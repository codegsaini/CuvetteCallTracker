package gaurav.cuvettecalltracker.presentation.log_detail

import gaurav.cuvettecalltracker.domain.model.CallLog

data class LogDetailState(
    val callLogs: List<CallLog> = emptyList()
)