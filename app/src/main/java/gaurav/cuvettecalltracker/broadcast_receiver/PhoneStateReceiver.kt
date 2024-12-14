package gaurav.cuvettecalltracker.broadcast_receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import gaurav.cuvettecalltracker.data.CallLogRepository
import gaurav.cuvettecalltracker.presentation.util.CallType
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PhoneStateReceiver: BroadcastReceiver() {

    @Inject
    lateinit var repository: CallLogRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return
        if (intent.action != TelephonyManager.ACTION_PHONE_STATE_CHANGED) return

        @Suppress("DEPRECATION")
        val number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
        if (number == null || state == null) return
        createCallLog(number, state)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun createCallLog(number: String, state: String) {
        GlobalScope.launch {
            repository.createCallLog(number, state)
        }
    }
}