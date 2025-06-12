package com.example.AbgabeMobile.QRScanner

import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.AbgabeMobile.data.Contact
import com.example.AbgabeMobile.data.ContactRepository
import com.google.gson.Gson
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class QRScanViewModel(
    private val contactRepository: ContactRepository? = null
) : ViewModel() {

    private val _hasCameraPermission = MutableStateFlow(false)
    val hasCameraPermission: StateFlow<Boolean> = _hasCameraPermission.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _scannedContactEvent = Channel<Contact>()
    val scannedContactEvent = _scannedContactEvent.receiveAsFlow()

    private val gson = Gson()
    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_AZTEC
        )
        .build()
    private val scanner = BarcodeScanning.getClient(options)

    private var cameraProviderInstance: ProcessCameraProvider? = null

    fun onPermissionResult(granted: Boolean) {
        _hasCameraPermission.value = granted
    }

    @OptIn(ExperimentalGetImage::class)
    fun bindCameraUseCases(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val currentCameraProvider = cameraProviderFuture.get()
            this.cameraProviderInstance = currentCameraProvider

            if (currentCameraProvider == null) {
                Log.e("QRScanViewModel", "CameraProvider is null.")
                return@addListener
            }

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                        if (_isProcessing.value) {
                            imageProxy.close()
                            return@setAnalyzer
                        }
                        val mediaImage = imageProxy.image
                        if (mediaImage != null) {
                            _isProcessing.value = true
                            val image = InputImage.fromMediaImage(
                                mediaImage,
                                imageProxy.imageInfo.rotationDegrees
                            )
                            scanner.process(image)
                                .addOnSuccessListener { barcodes ->
                                    for (barcode in barcodes) {
                                        val raw = barcode.rawValue ?: barcode.rawBytes?.toString(Charsets.UTF_8)
                                        if (raw != null) {
                                            try {
                                                val contact = gson.fromJson(raw, Contact::class.java)
                                                viewModelScope.launch {
                                                    _scannedContactEvent.send(contact)
                                                }
                                            } catch (e: Exception) {
                                                Log.e("QRScannerViewModel", "Parsing Error: ${e.message}", e)
                                            }
                                        } else {
                                            Log.e("QRScannerViewModel", "QR Code cant be read.")
                                        }
                                    }
                                }
                                .addOnCompleteListener {
                                    imageProxy.close()
                                    _isProcessing.value = false
                                }
                        } else {
                            imageProxy.close()
                        }
                    }
                }

            try {
                currentCameraProvider.unbindAll()
                currentCameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalyzer
                )
            } catch (exc: Exception) {
                Log.e("QRScanViewModel", "Binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    override fun onCleared() {
        super.onCleared()
        cameraProviderInstance?.unbindAll()
        Log.d("QRScanViewModel", "Camera provider unbound.")
        cameraProviderInstance = null
    }

    class Factory(private val contactRepository: ContactRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(QRScanViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return QRScanViewModel(contactRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}