package gaurav.cuvettecalltracker.presentation.home

sealed interface HomeScreenEvent {
    data class OnEnableCallRecording(val callback: (String) -> Unit): HomeScreenEvent
    data class OnDisableCallRecording(val callback: (String) -> Unit): HomeScreenEvent
}