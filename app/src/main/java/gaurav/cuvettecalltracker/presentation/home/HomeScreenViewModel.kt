package gaurav.cuvettecalltracker.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gaurav.cuvettecalltracker.data.CallLogRepository
import gaurav.cuvettecalltracker.data.DataStoreRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: CallLogRepository,
    private val dataStoreRepository: DataStoreRepository
): ViewModel() {

    private val _state = mutableStateOf(HomeScreenState())
    var state: State<HomeScreenState> = _state

    init {
        getRecentLogs()
        getCallRecordingEnabled()
    }

    fun onEvent(event: HomeScreenEvent) {
        when(event) {
            HomeScreenEvent.OnDisableCallRecording -> setCallRecordingState(false)
            HomeScreenEvent.OnEnableCallRecording -> setCallRecordingState(true)
        }
    }

    private fun getRecentLogs() {
        repository.getRecentLogs().onEach {
            _state.value = state.value.copy(
                recentLogs = it
            )
        }
            .launchIn(viewModelScope)
    }


    private fun getCallRecordingEnabled() {
        dataStoreRepository.getValue(
            booleanPreferencesKey("call_recording_enabled"),
            false
        ).onEach {
            _state.value = state.value.copy(
                isCallRecordingEnabled = it
            )
        }
            .launchIn(viewModelScope)
    }

    private fun setCallRecordingState(state: Boolean) {
        viewModelScope.launch {
            dataStoreRepository.setValue(booleanPreferencesKey("call_recording_enabled"), state)
        }
    }

}