package gaurav.cuvettecalltracker.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import gaurav.cuvettecalltracker.domain.model.CallLog
import gaurav.cuvettecalltracker.presentation.composables.CallLogCard
import gaurav.cuvettecalltracker.presentation.composables.Label
import gaurav.cuvettecalltracker.presentation.home.composables.AnalyticsRow
import gaurav.cuvettecalltracker.presentation.util.CallType

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val recentLogs = viewModel.state.value.recentLogs
    val totalCallLogs = recentLogs.size
    val totalIncomingCallLogs = recentLogs.filter { it.callType == CallType.INCOMING }.size
    val totalOutgoingCallLogs = recentLogs.filter { it.callType == CallType.OUTGOING }.size
    val totalMissedCallLogs = recentLogs.filter { it.callType == CallType.MISSED }.size
    val totalDuration = recentLogs.sumOf { it.duration }

    LazyColumn(
        Modifier
            .statusBarsPadding()
            .fillMaxSize(),
        contentPadding = PaddingValues(
            vertical = 15.dp,
            horizontal = 10.dp
        ),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        item {
            AnalyticsRow(
                modifier = Modifier.padding(horizontal = 5.dp),
                totalCalls = totalCallLogs,
                totalIncomingCalls = totalIncomingCallLogs,
                totalOutgoingCalls = totalOutgoingCallLogs,
                totalMissedCalls = totalMissedCallLogs,
                totalDuration = totalDuration
            )
        }
        item {
            Label(
                text = "Recent logs",
                modifier = Modifier.padding(
                    top = 20.dp,
                    bottom = 10.dp,
                    start = 5.dp
                )
            )
        }
        items(recentLogs) {
            CallLogCard(
                it,
                onClick = {}
            )
        }
    }
}