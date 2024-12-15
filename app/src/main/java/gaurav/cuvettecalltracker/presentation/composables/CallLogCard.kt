package gaurav.cuvettecalltracker.presentation.composables

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gaurav.cuvettecalltracker.R
import gaurav.cuvettecalltracker.domain.model.CallLog
import gaurav.cuvettecalltracker.presentation.util.CallLogResourceHelper.Companion.getCallTypeIconResource
import gaurav.cuvettecalltracker.presentation.util.CallLogResourceHelper.Companion.getCallTypeIconTint
import gaurav.cuvettecalltracker.presentation.util.CallLogResourceHelper.Companion.getCallTypeLabel
import gaurav.cuvettecalltracker.presentation.util.CallType
import gaurav.cuvettecalltracker.presentation.util.TimestampHelper.Companion.getSimpleDurationFormat
import gaurav.cuvettecalltracker.presentation.util.TimestampHelper.Companion.getSimpleTimeFormat

@Composable
fun CallLogCard(
    callLog: CallLog,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    val context = LocalContext.current
    val callTypeIconResource = getCallTypeIconResource(callLog.callType)
    val callTypeIconTint = getCallTypeIconTint(callLog.callType)
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable {
                if (callLog.active)
                    Toast.makeText(context, "Call is active", Toast.LENGTH_SHORT).show()
                else onClick(callLog.number)
            }
            .padding(horizontal = 5.dp)
            .padding(top = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            painter = painterResource(callTypeIconResource),
            contentDescription = null,
            modifier = Modifier
                .size(38.dp)
                .background(Color(0xFFEFEFEF), CircleShape)
                .padding(10.dp),
            tint = callTypeIconTint
        )
        Column(
            Modifier.weight(1f)
        ) {
            Text(
                text = callLog.number,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
            var label = if (callLog.callType == CallType.MISSED) getCallTypeLabel(callLog.callType)
            else getCallTypeLabel(callLog.callType)+ ", " +
                    getSimpleDurationFormat(callLog.duration)

            label = if (callLog.active) "Active" else label
            Text(
                text = label,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.sp,
                color = if (callLog.active) Color(0xFF42C759) else Color(0xFF565656)
            )
            Text(
                text = getSimpleTimeFormat(callLog.timestamp),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.sp,
                color = Color(0xFF565656)
            )
            Spacer(Modifier.height(10.dp))
            HorizontalDivider(
                Modifier.fillMaxWidth(),
                color = Color(0xFFE0E0E0),
                thickness = .8.dp
            )
        }
        Column(
            modifier = Modifier
                .padding(vertical = 5.dp),
            horizontalAlignment = Alignment.End
        ) {
            Icon(
                painter = painterResource(R.drawable.sim),
                contentDescription = null,
                modifier = Modifier
                    .size(18.dp),
                tint = Color.DarkGray
            )
            Text(
                text = "SIM 1",
                fontSize = 11.sp,
                letterSpacing = 0.sp,
                color = Color.Gray
            )
        }
    }
}