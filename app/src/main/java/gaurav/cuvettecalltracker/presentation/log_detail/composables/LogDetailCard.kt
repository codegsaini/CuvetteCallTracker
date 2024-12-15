package gaurav.cuvettecalltracker.presentation.log_detail.composables

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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
fun LogDetailCard(
    callLog: CallLog,
    modifier: Modifier = Modifier,
    playingRecording: Boolean = false,
    onListen: (CallLog) -> Unit,
    onStopListening: () -> Unit
) {
    val callTypeIconResource = getCallTypeIconResource(callLog.callType)
    val callTypeIconTint = getCallTypeIconTint(callLog.callType)
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(
                color = if (playingRecording) Color(0xFF42C759) else Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(top = 10.dp)
            .padding(horizontal = 10.dp),
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
                text = getSimpleTimeFormat(callLog.timestamp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp,
                color = if (playingRecording) Color.White else Color.Black
            )
            val label = if (callLog.callType == CallType.MISSED) getCallTypeLabel(callLog.callType)
            else getCallTypeLabel(callLog.callType)+ ", " +
                    getSimpleDurationFormat(callLog.duration)
            Text(
                text = label,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.sp,
                color = if (playingRecording) Color.White else Color(0xFF565656)
            )
            Spacer(Modifier.height(13.dp))
            HorizontalDivider(
                Modifier.fillMaxWidth(),
                color = Color(0xFFE0E0E0),
                thickness = .8.dp
            )
        }

        if (callLog.callType == CallType.MISSED) return@Row
        Column(
            modifier = Modifier
                .padding(vertical = 2.dp, horizontal = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                modifier = Modifier.size(34.dp),
                onClick = {
                    if (playingRecording) onStopListening()
                    else onListen(callLog)
                }
            ) {
                Icon(
                    painter = painterResource(
                        if (playingRecording) R.drawable.baseline_stop_24
                        else R.drawable.baseline_play_arrow_24
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .background(
                            color = Color(0xFFEFEFEF),
                            shape = CircleShape
                        )
                        .padding(7.dp),
                    tint = Color.DarkGray
                )
            }
        }
    }
}