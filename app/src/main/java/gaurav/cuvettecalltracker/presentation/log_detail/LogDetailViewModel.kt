package gaurav.cuvettecalltracker.presentation.log_detail

import android.content.Context
import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gaurav.cuvettecalltracker.data.CallLogRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File
import java.lang.NullPointerException
import javax.inject.Inject

@HiltViewModel
class LogDetailViewModel @Inject constructor(
    private val repository: CallLogRepository
): ViewModel() {

    private val _state = mutableStateOf(LogDetailState())
    var state: State<LogDetailState> = _state

    private var player: MediaPlayer? = null

    fun onEvent(event: LogDetailScreenEvent) {
        when(event) {
            is LogDetailScreenEvent.OnGetCallHistory -> getCallHistory(event.number)
            is LogDetailScreenEvent.OnStartPlayingRecording ->
                startPlaying(event.context, event.logId, event.file, event.onError)
            LogDetailScreenEvent.OnStopPlayingRecording -> stopPlaying()
        }
    }

    private fun getCallHistory(number: String) {
        repository.getCallHistory(number).onEach {
            _state.value = state.value.copy(
                callLogs = it
            )
        }
            .launchIn(viewModelScope)
    }

    private fun startPlaying(
        context: Context,
        logId: Int,
        file: File,
        onError: (String) -> Unit
    ) {
        stopPlaying()
        MediaPlayer.create(context, file.toUri()).apply {
            player = this
            try {
                start()
                _state.value = state.value.copy(
                    currentPlayingLogId = logId
                )
                setMediaPlayerListener()
            }
            catch (e: NullPointerException) {
                onError("Recording not found")
            }
        }
    }

    private fun stopPlaying() {
        player?.stop()
        player?.release()
        player = null
        _state.value = state.value.copy(
            currentPlayingLogId = -1
        )
    }

    private fun setMediaPlayerListener() {
        player?.setOnCompletionListener {
            stopPlaying()
        }
    }

}