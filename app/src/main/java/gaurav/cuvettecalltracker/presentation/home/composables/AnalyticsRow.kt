package gaurav.cuvettecalltracker.presentation.home.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import gaurav.cuvettecalltracker.R
import gaurav.cuvettecalltracker.presentation.composables.AnalyticsCard
import gaurav.cuvettecalltracker.presentation.util.CallLogResourceHelper.Companion.getCallTypeIconResource
import gaurav.cuvettecalltracker.presentation.util.CallLogResourceHelper.Companion.getCallTypeIconTint
import gaurav.cuvettecalltracker.presentation.util.CallType
import gaurav.cuvettecalltracker.presentation.util.TimestampHelper

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AnalyticsRow(
    modifier: Modifier = Modifier,
    totalCalls: Int = 0,
    totalIncomingCalls: Int = 0,
    totalOutgoingCalls: Int = 0,
    totalMissedCalls: Int = 0,
    totalDuration: Int = 0,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        maxItemsInEachRow = 2
    ) {
        AnalyticsCard(
            title = "Total Calls",
            value = "$totalCalls",
            iconResourceId = R.drawable.layers_24,
            iconTint = Color(0xFF313630)
        )
        AnalyticsCard(
            title = "Total duration",
            value = TimestampHelper.getSimpleDurationFormat(totalDuration),
            iconResourceId = R.drawable.clock_three_24,
            iconTint = Color(0xFF313630)
        )
        AnalyticsCard(
            title = "Incoming",
            value = "$totalIncomingCalls",
            iconResourceId = getCallTypeIconResource(CallType.INCOMING),
            iconTint = getCallTypeIconTint(CallType.INCOMING)
        )
        AnalyticsCard(
            title = "Outgoing",
            value = "$totalOutgoingCalls",
            iconResourceId = getCallTypeIconResource(CallType.OUTGOING),
            iconTint = getCallTypeIconTint(CallType.OUTGOING)
        )
        AnalyticsCard(
            title = "Missed",
            value = "$totalMissedCalls",
            iconResourceId = getCallTypeIconResource(CallType.MISSED),
            iconTint = getCallTypeIconTint(CallType.MISSED)
        )
        AllLogsCardButton(
            modifier = Modifier
                .weight(1f),
            onClick = {}
        )
    }
}