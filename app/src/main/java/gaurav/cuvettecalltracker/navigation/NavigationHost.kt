package gaurav.cuvettecalltracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import gaurav.cuvettecalltracker.presentation.home.HomeScreen
import gaurav.cuvettecalltracker.presentation.log_detail.LogDetailScreen

@Composable
fun NavigationHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController, Screen.LogDetail("")) {
        composable<Screen.Home> {
            HomeScreen()
        }

        composable<Screen.LogDetail> {
            LogDetailScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}