package com.example.AbgabeMobile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val titel: String, val icon: ImageVector) {

    object AddContact : Screen("addContact", "Add", Icons.Default.Add)

    object ContactList : Screen("contactList", "Contact", Icons.Default.List)

    object Detail : Screen("contact_detail", "Details", Icons.Default.AccountBox) {
        fun createRoute(contactId: Int) = "contact_detail/$contactId"
    }

    object QRScanner : Screen("qrScanner", "Scan", Icons.Default.Add)

    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}