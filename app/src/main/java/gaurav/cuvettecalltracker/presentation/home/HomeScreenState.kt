package gaurav.cuvettecalltracker.presentation.home

import gaurav.cuvettecalltracker.domain.model.CallLog

data class HomeScreenState(
    val recentLogs: List<CallLog> = emptyList(),
    val isCallRecordingEnabled: Boolean = false,
    val loadedCallRecordingStatusFromPreferences: Boolean = false,
    val isCallRecordingActive: Boolean = false
)