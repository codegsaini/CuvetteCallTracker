package gaurav.cuvettecalltracker.presentation.home

sealed interface HomeScreenEvent {
    data object OnEnableCallRecording: HomeScreenEvent
    data object OnDisableCallRecording: HomeScreenEvent
}