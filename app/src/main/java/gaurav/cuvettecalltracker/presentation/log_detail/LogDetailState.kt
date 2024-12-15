package gaurav.cuvettecalltracker.presentation.log_detail

import android.media.MediaPlayer
import gaurav.cuvettecalltracker.domain.model.CallLog

data class LogDetailState(
    val callLogs: List<CallLog> = emptyList(),
    val mediaPlayer: MediaPlayer? = null,
    val currentPlayingLogId: Int = -1
)