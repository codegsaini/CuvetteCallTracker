package gaurav.cuvettecalltracker.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.booleanPreferencesKey
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
        observeRecentLogs()
        observeCallRecordingEnabledStatus()
        observeCallRecordingActiveStatus()
    }

    fun onEvent(event: HomeScreenEvent) {
        when(event) {
            is HomeScreenEvent.OnDisableCallRecording ->
                setCallRecordingStatePreference(false, event.callback)
            is HomeScreenEvent.OnEnableCallRecording ->
                setCallRecordingStatePreference(true, event.callback)
        }
    }

    private fun observeRecentLogs() {
        repository.getRecentLogs().onEach {
            _state.value = state.value.copy(
                recentLogs = it
            )
        }
            .launchIn(viewModelScope)
    }


    private fun observeCallRecordingEnabledStatus() {
        dataStoreRepository.getValue(
            booleanPreferencesKey("call_recording_enabled"),
            false
        ).onEach {
            setIsCallRecordingEnabledState(it)
        }
            .launchIn(viewModelScope)
    }

    private fun setIsCallRecordingEnabledState(value: Boolean) {
        _state.value = state.value.copy(
            isCallRecordingEnabled = value,
            loadedCallRecordingStatusFromPreferences = true
        )
    }

    private fun setCallRecordingStatePreference(value: Boolean, onError: (String) -> Unit) {
        if (state.value.isCallRecordingActive) return onError("Can't change when call is active")
        viewModelScope.launch {
            dataStoreRepository.setValue(booleanPreferencesKey("call_recording_enabled"), value)
        }
    }

    private fun observeCallRecordingActiveStatus() {
        dataStoreRepository.getValue(
            key = booleanPreferencesKey("last_telephony_recording_state"),
            defaultValue = false
        ).onEach {
            _state.value = state.value.copy(
                isCallRecordingActive = it
            )
        }.launchIn(viewModelScope)
    }

}