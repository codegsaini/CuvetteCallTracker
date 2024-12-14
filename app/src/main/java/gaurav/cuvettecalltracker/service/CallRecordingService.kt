package gaurav.cuvettecalltracker.service

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.MediaDataSource
import android.media.MediaRecorder
import android.os.Build
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat.startForeground
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.AndroidEntryPoint
import gaurav.cuvettecalltracker.R
import gaurav.cuvettecalltracker.data.DataStoreRepository
import gaurav.cuvettecalltracker.presentation.util.TimestampHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class CallRecordingService: Service() {

    @Inject
    lateinit var dataStoreRepository: DataStoreRepository

    private var mediaRecorder: MediaRecorder? = null

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
        val shouldStartRecording = dataStoreRepository.getValue(
            booleanPreferencesKey("last_telephony_recording_state"),
            false
        )

        val callRecordingScope = CoroutineScope(Dispatchers.IO)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var lastRecordState = false
        shouldStartRecording.onEach  { record ->
            if (lastRecordState == record) return@onEach
            lastRecordState = record
            if (record) {
                initiateRecording()
                notificationManager.notify(
                    1,
                    getRecordingNotification(
                        123 // update duration
                    )
                )
            }
            else {
                mediaRecorder?.stop()
                mediaRecorder?.reset()
                mediaRecorder = null
                notificationManager.notify(
                    1,
                    getNotification(
                        "Cuvette Call Tracker",
                        "Assignment by Gaurav"
                    )
                )
            }
        }.distinctUntilChanged().launchIn(callRecordingScope)


    }

    private fun initiateRecording() {
        val number = runBlocking {
            dataStoreRepository.getValue(
                stringPreferencesKey("last_telephony_number"),
                "0"
            ).first()
        }
        val timestamp = runBlocking {
            dataStoreRepository.getValue(
                longPreferencesKey("last_call_log_timestamp"),
                -1L
            ).first()
        }
//        val contentValues = ContentValues().apply {
//            put(MediaStore.Audio.Media.TITLE, "Call_recording_$number.m4a")
//            put(MediaStore.Audio.Media.DATE_ADDED, (System.currentTimeMillis()/ 1000).toInt())
//        }

//        val audioUri =
//            contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues)
//                ?: return
        val file = getExternalFilesDir("Recordings")?.absolutePath ?: return

        val recorder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) MediaRecorder(applicationContext)
            else MediaRecorder()

        recorder.apply {
            setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION)
            setOutputFormat(MediaRecorder.OutputFormat.AMR_NB)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile("$file/CallRecording_${number}_$timestamp.m4a")
            Log.d("TTTG", "initiateRecording: $file/CallRecording_${number}_$timestamp.m4a")

            try { prepare() }
            catch (e: IOException) {
                Log.e("CallRecordingService", "initiateRecording: ${e.message}")
            } catch (e: IllegalStateException) {
                Log.e("CallRecordingService", "initiateRecording: ${e.message}")
            }
            start()
            mediaRecorder = this
        }
    }

    private fun startService() {
        val notification = getNotification(
            "Cuvette Call Tracker",
            "Assignment by Gaurav"
        )
        startForeground(1, notification)
    }

    private fun getNotification(title: String, text: String) : Notification {
        return NotificationCompat.Builder(this, "call_recording_service_notification_channel")
            .setContentTitle(title)
            .setContentText(text)
            .setOnlyAlertOnce(true)
            .setSmallIcon(R.drawable.baseline_person_24)
            .build()
    }

    private fun getRecordingNotification(duration: Int) : Notification {
        return NotificationCompat.Builder(this, "call_recording_service_notification_channel")
            .setContentTitle("Call Recording ...")
            .setContentText(TimestampHelper.getSimpleDurationFormat(duration))
            .setOnlyAlertOnce(true)
            .setSmallIcon(R.drawable.baseline_mic_24)
            .build()
    }
}