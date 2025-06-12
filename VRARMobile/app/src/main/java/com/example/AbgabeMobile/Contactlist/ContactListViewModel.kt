package com.example.AbgabeMobile.Contactlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.AbgabeMobile.data.Contact
import com.example.AbgabeMobile.data.ContactRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Represents the UI state for the contact list screen.
data class ContactListUiState(val contactList: List<Contact> = emptyList())

// ViewModel for the contact list screen.
class ContactListViewModel(private val contactRepository: ContactRepository) : ViewModel() {

    // Exposes a StateFlow that emits the current UI state for the contact list.
    // This StateFlow is derived from the contact repository's stream of all contacts.
    val uiState: StateFlow<ContactListUiState> =
        contactRepository.getAllContactsStream()
            .map { ContactListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ContactListUiState()
            ) // The StateFlow that provides the list of contacts for the UI

    // Adds a new contact to the repository.
    suspend fun addContact(contact: Contact): Int {
        val newId = contactRepository.insertContact(contact)
        return newId.toInt()
    }

    // Deletes an existing contact from the repository.
    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            contactRepository.deleteContact(contact)
        }
    }

    // Refreshes the contacts from the API by calling the repository.
    fun refreshContacts() {
        viewModelScope.launch {
            contactRepository.refreshContacts()
        }
    }

    // Updates an existing contact in the repository.
    fun updateContact(contact: Contact) {
        viewModelScope.launch {
            contactRepository.updateContact(contact)
        }
    }

    // Companion object to provide a ViewModelProvider.Factory for creating instances of ContactListViewModel.
    companion object {
        fun create(contactRepository: ContactRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(ContactListViewModel::class.java)) {
                        return ContactListViewModel(contactRepository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}