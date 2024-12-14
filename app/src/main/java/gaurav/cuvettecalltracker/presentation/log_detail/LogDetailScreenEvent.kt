package gaurav.cuvettecalltracker.presentation.log_detail

sealed interface LogDetailScreenEvent {
    data class OnGetCallHistory(val number: String): LogDetailScreenEvent
}