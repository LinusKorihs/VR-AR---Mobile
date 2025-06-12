package com.example.AbgabeMobile.Details

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.AbgabeMobile.QRScanner.generateQrCodeBitmap
import com.google.gson.Gson

@Composable
fun ContactDetailScreen(
    contactDetailViewModel: ContactDetailViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by contactDetailViewModel.uiState.collectAsStateWithLifecycle()
    val contact = uiState.contact

    var showQrCodeDialog by remember { mutableStateOf(false) }
    var qrCodeBitmap: Bitmap? by remember { mutableStateOf(null) }
    var serializedContactData: String? by remember { mutableStateOf(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (contact != null) {
            AsyncImage(
                model = contact.imageRes,
                contentDescription = "Profile picture of ${contact.name}",
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Text(
                text = "Details for: ${contact.name}",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Number: ${contact.phone}",
                fontSize = 18.sp
            )
            Text(
                text = "E-Mail: ${contact.email}",
                fontSize = 18.sp
            )
            Text(
                text = "Address: ${contact.street}",
                fontSize = 18.sp
            )
            Text(
                text = "House number.: ${contact.houseNr}",
                fontSize = 18.sp
            )
            Text(
                text = "Code: ${contact.postcode}",
                fontSize = 18.sp
            )
            Text(
                text = "City: ${contact.city}",
                fontSize = 18.sp
            )

            Spacer(Modifier.height(24.dp))

            Button(onClick = onNavigateBack) {
                Text("Back to list")
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    if (contact != null) {
                        serializedContactData = Gson().toJson(contact)
                        qrCodeBitmap = serializedContactData?.let { generateQrCodeBitmap(it) }
                        showQrCodeDialog = true
                    }
                }
            ) {
                Text("Generate QR-Code")
            }

            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                }
            ) {
                Text("Delete Contact")
            }

        } else {
            Text(text = "Contact not found.")
        }
    }

    if (showQrCodeDialog) {
        Dialog(onDismissRequest = { showQrCodeDialog = false }) {
            Surface(shape = MaterialTheme.shapes.medium) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "QR-Code for ${contact?.name}", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(16.dp))
                    qrCodeBitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "QR Code for ${contact?.name}",
                            modifier = Modifier.size(250.dp)
                        )
                    } ?: Text("QR-Code could not be generated.")
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { showQrCodeDialog = false }) {
                        Text("Close")
                    }
                }
            }
        }
    }
}



