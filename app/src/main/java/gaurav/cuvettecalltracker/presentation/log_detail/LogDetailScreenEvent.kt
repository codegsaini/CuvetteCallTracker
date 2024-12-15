package gaurav.cuvettecalltracker.presentation.log_detail

import android.content.Context
import java.io.File

sealed interface LogDetailScreenEvent {
    data class OnGetCallHistory(val number: String): LogDetailScreenEvent
    data class OnStartPlayingRecording(
        val context: Context,
        val logId: Int,
        val file: File,
        val onError: (String) -> Unit
    ): LogDetailScreenEvent
    data object OnStopPlayingRecording: LogDetailScreenEvent
}