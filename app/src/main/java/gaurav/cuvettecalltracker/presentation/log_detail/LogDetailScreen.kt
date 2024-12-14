package gaurav.cuvettecalltracker.presentation.log_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import gaurav.cuvettecalltracker.domain.model.CallLog
import gaurav.cuvettecalltracker.presentation.composables.Label
import gaurav.cuvettecalltracker.presentation.log_detail.composables.LogDetailCard
import gaurav.cuvettecalltracker.presentation.log_detail.composables.ProfileCard
import gaurav.cuvettecalltracker.presentation.log_detail.composables.TitleRow
import gaurav.cuvettecalltracker.presentation.util.CallType

@Composable
fun LogDetailScreen(
    number: String,
    onBackClick: () -> Unit,
    viewModel: LogDetailViewModel = hiltViewModel()
) {
    val callLogs = viewModel.state.value.callLogs
    val totalCallLogs = callLogs.size
    val totalIncomingCallLogs = callLogs.filter { it.callType == CallType.INCOMING }.size
    val totalOutgoingCallLogs = callLogs.filter { it.callType == CallType.OUTGOING }.size
    val totalMissedCallLogs = callLogs.filter { it.callType == CallType.MISSED }.size
    val totalDuration = callLogs.sumOf { it.duration }
    val totalIncomingDuration = callLogs.filter { it.callType == CallType.INCOMING }.sumOf { it.duration }
    val totalOutgoingDuration = callLogs.filter { it.callType == CallType.OUTGOING }.sumOf { it.duration }

    LaunchedEffect(number) {
        viewModel.onEvent(LogDetailScreenEvent.OnGetCallHistory(number))
    }

    Column(
        Modifier.statusBarsPadding()
    ) {
        TitleRow(onBackClick = onBackClick)
        LazyColumn(
            contentPadding = PaddingValues(
                vertical = 15.dp,
                horizontal = 10.dp
            ),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            item {
                ProfileCard(
                    number = number,
                    totalCalls = totalCallLogs,
                    totalIncomingCalls = totalIncomingCallLogs,
                    totalOutgoingCalls = totalOutgoingCallLogs,
                    totalMissedCalls = totalMissedCallLogs,
                    totalDuration = totalDuration,
                    totalIncomingDuration = totalIncomingDuration,
                    totalOutgoingDuration = totalOutgoingDuration
                )
            }
            item {
                Label(
                    text = "Call history",
                    modifier = Modifier.padding(
                        top = 30.dp,
                        bottom = 10.dp,
                        start = 5.dp
                    )
                )
            }
            items(callLogs) {
                LogDetailCard (
                    callLog = it,
                    onListenClick = {}
                )
            }
        }
    }
}