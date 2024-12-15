package gaurav.cuvettecalltracker.service

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaRecorder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.AndroidEntryPoint
import gaurav.cuvettecalltracker.R
import gaurav.cuvettecalltracker.data.DataStoreRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class CallRecordingService: Service() {

    @Inject
    lateinit var mDataStoreRepository: DataStoreRepository

    private var mMediaRecorder: MediaRecorder? = null
    private var mNumber: String = ""
    private var mTimestamp: Long = -1L
    private var mFile: String? = null
    private var mNotificationManager: NotificationManager? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            "START" -> startService()
            "STOP" -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        startService()

        val numberAndTimestampObserverScope =
            CoroutineScope(Dispatchers.IO + SupervisorJob())
        val callRecordingScope = CoroutineScope(Dispatchers.IO)

        mFile = getExternalFilesDir("Recordings")?.absolutePath
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        observerLastTelephonyNumber(numberAndTimestampObserverScope)
        observerLastCallLogTimestamp(numberAndTimestampObserverScope)
        observeRecordingActiveState(callRecordingScope)
    }

    private fun startService() {
        val notification = getNotification(
            title = "Cuvette Call Tracker",
            text = "Automatic call recording active",
            cancelable = false
        )
        startForeground(1, notification)
    }

    private fun observerLastTelephonyNumber(scope: CoroutineScope) {
        mDataStoreRepository.getValue(
            key = stringPreferencesKey("last_telephony_number"),
            defaultValue = "0"
        ).onEach {
            mNumber = it
        }.launchIn(scope)
    }

    private fun observerLastCallLogTimestamp(scope: CoroutineScope) {
        mDataStoreRepository.getValue(
            key = longPreferencesKey("last_call_log_timestamp"),
            defaultValue = -1L
        ).onEach {
            mTimestamp = it
        }.launchIn(scope)
    }

    private fun observeRecordingActiveState(scope: CoroutineScope) {
        val shouldStartRecording = mDataStoreRepository.getValue(
            booleanPreferencesKey("last_telephony_recording_state"),
            false
        )

        var lastRecordState = false
        shouldStartRecording.onEach  { record ->
            if (lastRecordState == record) return@onEach
            else lastRecordState = record

            val fileName = "$mFile/CallRecording_${mNumber}_$mTimestamp.m4a"

            if (record) {
                initiateRecording(fileName)
                mNotificationManager?.notify(
                    2,
                    getRecordingNotification()
                )
            } else {
                mMediaRecorder?.stop()
                mMediaRecorder?.reset()
                mMediaRecorder = null
                mNotificationManager?.cancel(2)
                mNotificationManager?.notify(
                    3,
                    getNotification(
                        title = "Recording saved",
                        text = fileName.split("/").last(),
                        cancelable = true
                    )
                )
            }
        }
            .launchIn(scope)
    }

    private fun getRecorder() : MediaRecorder =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) MediaRecorder(applicationContext)
        else MediaRecorder()

    private fun initiateRecording(fileName: String) {
        if (mFile == null) return
        getRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION)
            setOutputFormat(MediaRecorder.OutputFormat.AMR_NB)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(fileName)

            try { prepare() }
            catch (e: IOException) {
                Log.e("CallRecordingService", "initiateRecording: ${e.message}")
                return@apply
            } catch (e: IllegalStateException) {
                Log.e("CallRecordingService", "initiateRecording: ${e.message}")
                return@apply
            }
            start()
            mMediaRecorder = this
        }
    }

    private fun getNotification(title: String, text: String, cancelable: Boolean) : Notification {
        return NotificationCompat.Builder(
            this,
            "call_recording_service_notification_channel"
        )
            .setContentTitle(title)
            .setContentText(text)
            .setOnlyAlertOnce(true)
            .setOngoing(!cancelable)
            .setSmallIcon(R.drawable.baseline_person_24)
            .build()
    }

    private fun getRecordingNotification() : Notification {
        return NotificationCompat.Builder(
            this,
            "call_recording_service_notification_channel"
        )
            .setContentTitle("Call Recording...")
            .setUsesChronometer(true)
            .setOnlyAlertOnce(true)
            .setSmallIcon(R.drawable.baseline_mic_24)
            .build()
    }
}