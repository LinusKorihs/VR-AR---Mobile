package com.example.AbgabeMobile.QRScanner

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.view.PreviewView
import com.example.AbgabeMobile.data.Contact

@ExperimentalGetImage
@Composable
fun QRCodeScannerScreen(
    onScanSuccess: (Contact) -> Unit,
    onBack: () -> Unit,
    viewModel: QRScanViewModel = viewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val hasCameraPermission by viewModel.hasCameraPermission.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.scannedContactEvent.collect { contact ->
            onScanSuccess(contact)
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            viewModel.onPermissionResult(granted)
        }
    )

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }

    if (hasCameraPermission) {
        Box(modifier = Modifier.fillMaxSize()) {

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    viewModel.bindCameraUseCases(ctx, lifecycleOwner, previewView)
                    previewView
                }
            )

            Button(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back to main menu")
                Text(" Back")
            }

            if (isProcessing) {
            }
        }
    } else {
        Text("Camera access required.")
    }
}