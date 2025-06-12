package com.example.AbgabeMobile

import BottomNavigationBar
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
// Import for LocalContext
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.AbgabeMobile.Contactlist.ContactListScreen
import com.example.AbgabeMobile.Contactlist.ContactListViewModel
import com.example.AbgabeMobile.data.AppContainer
import com.example.AbgabeMobile.data.AppContainerImpl
import com.example.AbgabeMobile.Details.ContactDetailScreen
import com.example.AbgabeMobile.Details.ContactDetailViewModel
import com.example.AbgabeMobile.QRScanner.QRCodeScannerScreen
import com.example.AbgabeMobile.QRScanner.QRScanViewModel
import com.example.AbgabeMobile.Settings.SettingsScreen
import com.example.AbgabeMobile.Settings.SettingsViewModel
import com.example.AbgabeMobile.ui.theme.AbgabeMobileTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContainer = AppContainerImpl(applicationContext)

        setContent {
            AbgabeMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ContactApp(appContainer = appContainer)
                }
            }
        }
    }

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    @OptIn(ExperimentalGetImage::class, ExperimentalMaterial3Api::class)
    @Composable
    fun ContactApp(appContainer: AppContainer) {
        val navController = rememberNavController()

        Scaffold(
            bottomBar = {
                BottomNavigationBar(navController = navController)
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Screen.ContactList.route,
                modifier = Modifier.padding(paddingValues)
            ) {

                composable(Screen.ContactList.route) {
                    val contactListViewModel: ContactListViewModel = viewModel(
                        factory = ContactListViewModel.create(appContainer.contactRepository)
                    )
                    ContactListScreen(
                        contactListViewModel = contactListViewModel,
                        onContactClick = { contact ->
                            // Correct navigation to the detail view using the ID
                            navController.navigate(Screen.Detail.createRoute(contact.id))
                        }
                    )
                }

                composable(
                    route = Screen.Detail.route + "/{contactId}",
                    arguments = listOf(navArgument("contactId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val contactId = backStackEntry.arguments?.getInt("contactId")
                    if (contactId != null) {
                        val contactDetailViewModel: ContactDetailViewModel = viewModel(
                            factory = ContactDetailViewModel.create(
                                contactRepository = appContainer.contactRepository,
                                savedStateHandle = backStackEntry.savedStateHandle
                            )
                        )
                        ContactDetailScreen(
                            contactDetailViewModel = contactDetailViewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    } else {
                        Log.e("Navigation", "ContactDetailScreen called without contactId")
                        navController.popBackStack() // Or navigate to the contact list
                    }
                }

                composable(Screen.QRScanner.route) {
                    val contactListViewModel: ContactListViewModel = viewModel(
                        factory = ContactListViewModel.create(appContainer.contactRepository)
                    )
                    val qrScanViewModel: QRScanViewModel = viewModel(
                        factory = QRScanViewModel.Factory(appContainer.contactRepository)
                    )
                    QRCodeScannerScreen(
                        onScanSuccess = { contact ->
                            navController.currentBackStackEntry?.lifecycleScope?.launch {
                                val newContactId = contactListViewModel.addContact(contact)
                                navController.navigate(Screen.Detail.createRoute(newContactId)) {
                                    popUpTo(Screen.QRScanner.route) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        },
                        onBack = { navController.popBackStack() },
                        viewModel = qrScanViewModel
                    )
                }

                composable(Screen.Settings.route) {
                    // NEW WAY to get the Application context
                    val context = LocalContext.current
                    val application = context.applicationContext as Application

                    val settingsViewModel: SettingsViewModel = viewModel(
                        factory = SettingsViewModel.create(appContainer.contactRepository, application)
                    )
                    SettingsScreen(settingsViewModel = settingsViewModel)
                }
            }
        }
    }
}