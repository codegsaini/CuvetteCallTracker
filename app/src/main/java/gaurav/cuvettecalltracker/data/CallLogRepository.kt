package gaurav.cuvettecalltracker.data

import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import gaurav.cuvettecalltracker.domain.model.CallLog
import gaurav.cuvettecalltracker.presentation.util.CallType
import gaurav.cuvettecalltracker.room.dao.CallLogDao
import gaurav.cuvettecalltracker.service.CallRecordingService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class CallLogRepository @Inject constructor(
    private val context: Context,
    private val dataStoreRepository: DataStoreRepository,
    private val callLogDao: CallLogDao
) {

    suspend fun createCallLog(number: String, state: String) {
        val lastState = getLastTelephonyState()
        // Ignore second consecutive same broadcast
        if (lastState == state) return

        val recording = getRecordingState()
        val currentTimestamp = System.currentTimeMillis()
        val (currentCallType, isCallActiveCurrently, callSettled) = when (lastState) {
            TelephonyManager.EXTRA_STATE_IDLE -> {
                when (state) {
                    TelephonyManager.EXTRA_STATE_RINGING -> Triple(CallType.INCOMING, false, false)
                    TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                        if (!recording) {
                            startRecording(currentTimestamp)
                        }
                        Triple(CallType.OUTGOING, true, false)
                    }
                    else -> Triple(null, false, false)
                }
            }
            TelephonyManager.EXTRA_STATE_RINGING -> {
                when (state) {
                    TelephonyManager.EXTRA_STATE_IDLE -> Triple(CallType.MISSED, false, true)
                    TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                        if (!recording) {
                            startRecording(currentTimestamp)
                        }
                        Triple(CallType.INCOMING, true, false)
                    }
                    else -> Triple(null, false, false)
                }
            }
            TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                when (state) {
                    TelephonyManager.EXTRA_STATE_IDLE -> {
                        if (recording) {
                            stopRecording()
                        }
                        Triple(CallType.UNKNOWN, false, true)
                    }
                    else -> Triple(null, false, false)
                }
            }
            else -> Triple(null, false, false)
        }

        if (currentCallType == null) return

        val recentUnsettledCallLog = callLogDao.getRecentUnsettledLog(number).firstOrNull()

        if (recentUnsettledCallLog == null) {
            val callLog = CallLog(
                number = number,
                callType = currentCallType,
                active = isCallActiveCurrently,
                duration = 0,
                settled = callSettled,
                timestamp = currentTimestamp
            )
            callLogDao.addCallLog(callLog)
        } else {
            // Can't check `settled: Boolean` because it will also `true` on missed call
            val callFinished = recentUnsettledCallLog.active && state == TelephonyManager.EXTRA_STATE_IDLE

            val duration =
                if (callFinished) ((currentTimestamp - recentUnsettledCallLog.timestamp)/1000).toInt()
                else recentUnsettledCallLog.duration

            val callType = if (callFinished) recentUnsettledCallLog.callType else currentCallType

            val updatedCallLog = recentUnsettledCallLog.copy(
                callType = callType,
                active = isCallActiveCurrently,
                duration = duration,
                settled = callSettled
            )
            callLogDao.upsertCallLog(updatedCallLog)
        }
        setLastTelephonyState(state)
        setLastNumber(number)
        setLastStateChangeTimestamp(currentTimestamp)
    }

    fun getRecentLogs() : Flow<List<CallLog>> {
        return callLogDao.getRecentLogs()
    }

    private suspend fun getRecordingState() : Boolean =
        dataStoreRepository.getValue(
            key = booleanPreferencesKey("last_telephony_recording_state"),
            defaultValue = false
        ).first()

    private suspend fun setRecordingState(state: Boolean) {
        dataStoreRepository.setValue(
            key = booleanPreferencesKey("last_telephony_recording_state"),
            value = state
        )
    }
    private suspend fun getLastTelephonyState() : String =
        dataStoreRepository.getValue(
            key = stringPreferencesKey("last_telephony_state"),
            defaultValue = TelephonyManager.EXTRA_STATE_IDLE
        ).first()

    private suspend fun setLastTelephonyState(state: String) {
        dataStoreRepository.setValue(
            key = stringPreferencesKey("last_telephony_state"),
            value = state
        )
    }
    private suspend fun getLastNumber() : String =
        dataStoreRepository.getValue(
            key = stringPreferencesKey("last_telephony_number"),
            defaultValue = "0"
        ).first()

    private suspend fun setLastNumber(number: String) {
        dataStoreRepository.setValue(
            key = stringPreferencesKey("last_telephony_number"),
            value = number
        )
    }
    private suspend fun getLastStateChangeTimestamp() : Long =
        dataStoreRepository.getValue(
            key = longPreferencesKey("last_telephony_state_change_timestamp"),
            defaultValue = -1L
        ).first()

    private suspend fun setLastStateChangeTimestamp(currentTimestamp: Long) {
        dataStoreRepository.setValue(
            key = longPreferencesKey("last_telephony_state_change_timestamp"),
            value = currentTimestamp
        )
    }

    private suspend fun getCallLogTimestamp() : Long =
        dataStoreRepository.getValue(
            key = longPreferencesKey("last_call_log_timestamp"),
            defaultValue = -1L
        ).first()

    private suspend fun setCallLogTimestamp(currentTimestamp: Long) {
        dataStoreRepository.setValue(
            key = longPreferencesKey("last_call_log_timestamp"),
            value = currentTimestamp
        )
    }

    fun getTotalCalls() : Flow<Int> = callLogDao.getTotalLogs()
    fun getTotalIncomingCalls() : Flow<Int> = callLogDao.getTotalIncomingCallLogs()
    fun getTotalOutgoingCalls() : Flow<Int> = callLogDao.getTotalOutgoingCallLogs()
    fun getTotalMissedCalls() : Flow<Int> = callLogDao.getTotalMissedCallLogs()

    fun getCallHistory(number: String) : Flow<List<CallLog>> =
        callLogDao.getCallHistory(number)

    private suspend fun startRecording(timestamp: Long) {
        setCallLogTimestamp(timestamp)
        setRecordingState(true)
    }

    private suspend fun stopRecording() {
        setRecordingState(false)
    }
}