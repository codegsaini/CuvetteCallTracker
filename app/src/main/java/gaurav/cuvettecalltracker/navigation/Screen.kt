package gaurav.cuvettecalltracker.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Home: Screen
}