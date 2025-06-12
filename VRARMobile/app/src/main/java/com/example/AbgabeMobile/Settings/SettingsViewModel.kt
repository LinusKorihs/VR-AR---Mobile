package com.example.AbgabeMobile.Settings

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.AbgabeMobile.data.ContactRepository
import kotlinx.coroutines.launch

// ViewModel for managing settings-related actions, such as clearing contacts,
// refreshing contacts from an API, and exporting contacts.
class SettingsViewModel(private val contactRepository: ContactRepository, application: Application) : ViewModel() {
    private val appContext = application.applicationContext

    // Clears all contacts from the local database.
    fun clearAllContacts() {
        viewModelScope.launch {
            Log.d("SettingsViewModel", "deleteAllContacts() called in ViewModel.")
            contactRepository.deleteAllContacts() // Calls the method in the repository.
        }
    }

    // Exports contacts to the device's downloads folder.
    // Shows a Toast message indicating success or failure.
    fun exportContactsToDownloads() {
        viewModelScope.launch {
            val success = contactRepository.exportContactsToDownloads(appContext)
            if (success) {
                Toast.makeText(appContext, "Contacts exported successfully!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(appContext, "Error exporting contacts.", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Refreshes contacts from the API and updates the local database.
    fun refreshContactsFromApi() {
        viewModelScope.launch {
            contactRepository.refreshContacts() // Calls the method in the repository.
        }
    }

    // ViewModelFactory to instantiate SettingsViewModel with the ContactRepository and Application.
    companion object {
        // The create method now also needs to receive the Application.
        fun create(contactRepository: ContactRepository, application: Application): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    // Check if the requested ViewModel class is SettingsViewModel.
                    if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        // Application to the constructor of SettingsViewModel.
                        return SettingsViewModel(contactRepository, application) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }

}