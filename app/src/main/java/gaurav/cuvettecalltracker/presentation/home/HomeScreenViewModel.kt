package gaurav.cuvettecalltracker.presentation.home

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
class HomeScreenViewModel @Inject constructor(
    val repository: CallLogRepository
): ViewModel() {

    private val _state = mutableStateOf(HomeScreenState())
    var state: State<HomeScreenState> = _state

    init {
        getRecentLogs()
    }

    private fun getRecentLogs() {
        repository.getRecentLogs().onEach {
            _state.value = state.value.copy(
                recentLogs = it
            )
        }
            .launchIn(viewModelScope)
    }

}