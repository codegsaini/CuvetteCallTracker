package gaurav.cuvettecalltracker.presentation.log_detail.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gaurav.cuvettecalltracker.R
import gaurav.cuvettecalltracker.presentation.composables.Chip
import gaurav.cuvettecalltracker.presentation.util.TimestampHelper

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileCard(
    number: String,
    totalCalls: Int,
    totalIncomingCalls: Int,
    totalOutgoingCalls: Int,
    totalMissedCalls: Int,
    totalDuration: Int,
    totalIncomingDuration: Int,
    totalOutgoingDuration: Int,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.baseline_person_24),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .aspectRatio(1f)
                .border(
                    width = 1.dp,
                    color = Color(0xFFEAEAEA),
                    shape = CircleShape
                )
                .background(
                    color = Color(0xFFEFEFEF),
                    shape = CircleShape
                )
                .padding(40.dp),
            colorFilter = ColorFilter.tint(Color.Gray)
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = "Gaurav Saini",// In future it can be retrieved from contact list
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.sp
        )
        Spacer(Modifier.height(5.dp))
        Text(
            text = number,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.sp
        )
        Spacer(Modifier.height(15.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Chip("$totalIncomingCalls Incoming, " +
                    TimestampHelper.getSimpleDurationFormat(totalIncomingDuration)
            )
            Chip("$totalOutgoingCalls Outgoing, " +
                    TimestampHelper.getSimpleDurationFormat(totalOutgoingDuration)
            )
            Chip("$totalMissedCalls Missed calls")
            Chip("Total calls: $totalCalls")
            Chip("Total duration: ${TimestampHelper.getSimpleDurationFormat(totalDuration)}")
        }
    }
}