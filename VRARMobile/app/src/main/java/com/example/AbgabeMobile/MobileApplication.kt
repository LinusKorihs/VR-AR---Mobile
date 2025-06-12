package com.example.AbgabeMobile

import android.app.Application
import com.example.AbgabeMobile.data.AppDatabase
import com.example.AbgabeMobile.data.ContactRepository
import com.example.AbgabeMobile.network.KtorService

// Application class for dependency injection and global state management.
class AbgabeMobileApplication : Application() {
    // Lazily initializes the AppDatabase singleton.
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    // Lazily initializes the ContactRepository with the DAO and KtorService.
    val contactRepository: ContactRepository by lazy {
        ContactRepository(database.contactDao(), ktorService)
    }
    // Lazily initializes the KtorService.
    val ktorService by lazy { KtorService() }
}