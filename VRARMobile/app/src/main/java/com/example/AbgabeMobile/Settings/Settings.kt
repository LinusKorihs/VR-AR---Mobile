package com.example.AbgabeMobile.Settings

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = viewModel(), // Default value, will be overwritten by MainActivity
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize() // Ensures the column takes up all available space
            .padding(16.dp),
    ) {
        Box {
            Text(
                text = "Settings",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(Modifier.height(24.dp))

        // Section to clear the database
        Text(
            text = "This button deletes all data in the database.",
        )
        Button(
            onClick = {
                Log.d("SettingsScreen", "Button 'Clear Database' clicked.")
                settingsViewModel.clearAllContacts() // Call ViewModel method
            },
        ) {
            Text("Clear Database")
        }
        Spacer(Modifier.height(24.dp))

        // Section to refresh contacts from API
        Text(
            text = "This button reloads contact data from the API and saves it to the database.",
        )
        Button(
            onClick = {
                settingsViewModel.refreshContactsFromApi() // Call ViewModel method
            },
        ) {
            Text("Reload Contacts (API)")
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Section to export contacts to downloads
        Text(
            text = "This button saves the contact data from the database locally to the PC.",
        )
        Button(
            onClick = {
                Log.d("SettingsScreen", "Button 'Clicked: Export Contacts.")
                settingsViewModel.exportContactsToDownloads()
            }
        ) {
            Text("Export Contacts (Downloads)")
        }
    }
}