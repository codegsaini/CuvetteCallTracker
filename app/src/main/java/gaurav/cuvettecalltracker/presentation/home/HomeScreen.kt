package gaurav.cuvettecalltracker.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gaurav.cuvettecalltracker.domain.model.CallLog
import gaurav.cuvettecalltracker.presentation.composables.CallLogCard
import gaurav.cuvettecalltracker.presentation.composables.Label
import gaurav.cuvettecalltracker.presentation.home.composables.AnalyticsRow
import gaurav.cuvettecalltracker.presentation.util.CallType

@Composable
fun HomeScreen() {
    LazyColumn(
        Modifier
            .statusBarsPadding()
            .fillMaxSize(),
        contentPadding = PaddingValues(
            vertical = 15.dp,
            horizontal = 10.dp
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            AnalyticsRow(Modifier.padding(horizontal = 5.dp))
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
        items(10) {
            CallLogCard(
                CallLog(
                    id = "",
                    contactId = "",
                    timestamp = 1734024741,
                    callType = CallType.OUTGOING,
                    duration = 3534,
                    sim = 1
                ),
                onClick = {}
            )
        }
    }
}