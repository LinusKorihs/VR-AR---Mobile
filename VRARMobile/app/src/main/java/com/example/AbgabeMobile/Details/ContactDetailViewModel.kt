package com.example.AbgabeMobile.Details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.AbgabeMobile.data.Contact
import com.example.AbgabeMobile.data.ContactRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ContactDetailUiState(val contact: Contact? = null)

class ContactDetailViewModel(
    private val contactRepository: ContactRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val contactId: Int = checkNotNull(savedStateHandle["contactId"])

    val uiState: StateFlow<ContactDetailUiState> =
        contactRepository.getContactStream(contactId)
            .filterNotNull()
            .map { ContactDetailUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ContactDetailUiState()
            )
    // Function to delete the displayed contact.
    fun deleteContact() {
        viewModelScope.launch {
            uiState.value.contact?.let { contactRepository.deleteContact(it) }
        }
    }

    // Function to update the displayed contact.
    fun updateContact(updatedContact: Contact) {
        viewModelScope.launch {
            contactRepository.updateContact(updatedContact)
        }
    }

    // ViewModelFactory to instantiate ContactDetailViewModel with the repository and SavedStateHandle.
    companion object {
        fun create(contactRepository: ContactRepository, savedStateHandle: SavedStateHandle): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(ContactDetailViewModel::class.java)) {
                        return ContactDetailViewModel(
                            contactRepository,
                            savedStateHandle
                        ) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}