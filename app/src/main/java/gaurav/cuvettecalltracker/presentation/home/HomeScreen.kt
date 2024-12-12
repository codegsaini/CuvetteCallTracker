package gaurav.cuvettecalltracker.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import gaurav.cuvettecalltracker.R
import gaurav.cuvettecalltracker.presentation.composables.AnalyticsCard

@Composable
fun HomeScreen() {
    LazyColumn(
        Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        item {
            AnalyticsRow()
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AnalyticsRow() {
    FlowRow(
        modifier = Modifier
            .padding(15.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        maxItemsInEachRow = 2
    ) {
        AnalyticsCard(
            title = "Total Calls",
            value = "233",
            iconResourceId = R.drawable.layers_24,
            iconTint = Color(0xFF313630)
        )
        AnalyticsCard(
            title = "Total duration",
            value = "334 Hours",
            iconResourceId = R.drawable.clock_three_24,
            iconTint = Color(0xFF313630)
        )
        AnalyticsCard(
            title = "Incoming",
            value = "150",
            iconResourceId = R.drawable.call_incoming_24,
            iconTint = Color(0xFF40A451)
        )
        AnalyticsCard(
            title = "Outgoing",
            value = "83",
            iconResourceId = R.drawable.call_outgoing_24,
            iconTint = Color(0xFF406DA4)
        )
        AnalyticsCard(
            title = "Missed",
            value = "83",
            iconResourceId = R.drawable.call_missed_24,
            iconTint = Color(0xFFD54545)
        )
        Spacer(Modifier.weight(1f))
    }
}