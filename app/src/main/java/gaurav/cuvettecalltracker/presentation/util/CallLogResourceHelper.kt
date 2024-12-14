package gaurav.cuvettecalltracker.presentation.util

import androidx.compose.ui.graphics.Color
import gaurav.cuvettecalltracker.R

class CallLogResourceHelper {
    companion object {
        fun getCallTypeIconResource(callType: CallType) : Int =
            callTypeIconResource.getOrDefault(callType, R.drawable.ic_launcher_foreground)

        fun getCallTypeIconTint(callType: CallType) : Color =
            callTypeIconTint.getOrDefault(callType, Color.Black)

        fun getCallTypeLabel(callType: CallType) : String =
            callTypeLabel.getOrDefault(callType, "")

        private val callTypeIconResource = mapOf(
            CallType.INCOMING to R.drawable.call_incoming_24,
            CallType.OUTGOING to R.drawable.call_outgoing_24,
            CallType.MISSED to R.drawable.call_missed_24,
        )
        private val callTypeIconTint = mapOf(
            CallType.INCOMING to Color(0xFF40A451),
            CallType.OUTGOING to Color(0xFF406DA4),
            CallType.MISSED to Color(0xFFD54545),
        )
        private val callTypeLabel = mapOf(
            CallType.INCOMING to "Incoming call",
            CallType.OUTGOING to "Outgoing call",
            CallType.MISSED to "Missed call"
        )
    }
}