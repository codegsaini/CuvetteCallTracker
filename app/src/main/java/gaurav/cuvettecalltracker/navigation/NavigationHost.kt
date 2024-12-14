package gaurav.cuvettecalltracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import gaurav.cuvettecalltracker.presentation.home.HomeScreen
import gaurav.cuvettecalltracker.presentation.log_detail.LogDetailScreen

@Composable
fun NavigationHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController, Screen.Home) {
        composable<Screen.Home> {
            HomeScreen(
                onCallLogClick = { navController.navigate(Screen.LogDetail(it)) }
            )
        }

        composable<Screen.LogDetail> {
            val data = it.toRoute<Screen.LogDetail>()
            LogDetailScreen(
                number = data.number,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}