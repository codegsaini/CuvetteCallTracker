package gaurav.cuvettecalltracker.presentation.log_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gaurav.cuvettecalltracker.data.CallLogRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LogDetailViewModel @Inject constructor(
    val repository: CallLogRepository
): ViewModel() {

    private val _state = mutableStateOf(LogDetailState())
    var state: State<LogDetailState> = _state

    fun onEvent(event: LogDetailScreenEvent) {
        when(event) {
            is LogDetailScreenEvent.OnGetCallHistory -> getCallHistory(event.number)
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

}