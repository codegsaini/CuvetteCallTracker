package gaurav.cuvettecalltracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import gaurav.cuvettecalltracker.presentation.home.HomeScreen

@Composable
fun NavigationHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController, Screen.Home) {
        composable<Screen.Home> {
            HomeScreen()
        }
    }
}