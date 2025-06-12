package com.example.AbgabeMobile.data

import android.content.Context
import com.example.AbgabeMobile.network.KtorService

class AppContainerImpl(private val applicationContext: Context) : AppContainer {

    private val ktorService: KtorService by lazy {
        KtorService()
    }

    override val contactRepository: ContactRepository by lazy {
        ContactRepository(
            contactDao = AppDatabase.getDatabase(applicationContext).contactDao(),
            ktorService = ktorService
        )
    }
}