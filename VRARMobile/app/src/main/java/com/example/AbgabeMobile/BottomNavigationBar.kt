import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.AbgabeMobile.Screen

// Composable function for the bottom navigation bar.
@Composable
fun BottomNavigationBar(navController: NavController) {
    // Defines the items for the navigation bar, including the QRScanner.
    val items = listOf(Screen.ContactList, Screen.QRScanner, Screen.Settings)
    val currentRoute: String? = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        items.forEach { screen: Screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationRoute ?: "") {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                label = { Text(text = screen.titel) },
                icon = { Icon(imageVector = screen.icon, contentDescription = null) }
            )
        }
    }
}