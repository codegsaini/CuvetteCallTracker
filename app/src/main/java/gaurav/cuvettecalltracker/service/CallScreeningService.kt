package gaurav.cuvettecalltracker.service

import android.os.Build
import android.telecom.Call
import android.telecom.CallScreeningService
import dagger.hilt.android.AndroidEntryPoint
import gaurav.cuvettecalltracker.data.CallLogRepository
import gaurav.cuvettecalltracker.presentation.util.CallType
import javax.inject.Inject

@AndroidEntryPoint
class CallScreeningService @Inject constructor(
    private val repository: CallLogRepository
) : CallScreeningService() {
    override fun onScreenCall(callDetails: Call.Details) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (callDetails.callDirection == Call.Details.DIRECTION_INCOMING) {
                val number = callDetails.handle.schemeSpecificPart
                //repository.createCallLog(number, CallType.OUTGOING)
            }
            respondToCall(callDetails, CallResponse.Builder().build())
        }
    }
}