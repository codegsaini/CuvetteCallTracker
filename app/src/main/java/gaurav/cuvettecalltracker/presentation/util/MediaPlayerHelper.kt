package gaurav.cuvettecalltracker.presentation.util

import android.content.Context
import android.media.MediaPlayer
import android.widget.Toast
import androidx.core.net.toUri
import java.io.File
import java.lang.NullPointerException

class MediaPlayerHelper(private val context: Context) {

    private var player: MediaPlayer? = null

    fun playAudio(file: File) {
        MediaPlayer.create(context, file.toUri()).apply {
            player = this
            try { start() } catch (e: NullPointerException) {
                Toast.makeText(context, "No recording", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun stop() {
        player?.stop()
        player?.release()
        player = null
    }

}