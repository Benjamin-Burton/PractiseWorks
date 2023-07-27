package burt.music.practiseworks.mediaService

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.content.res.Resources
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import com.example.practiseworks.R
import kotlinx.coroutines.delay
import java.io.File

/**
 * This class is the media player service that is used by practise tasks
 * in the bottom bar.
 * It requires a context variable - this doesn't seem to match well with
 * the way compose handles lifecycle - requires more research to get this right.
 * At the moment, if a track is playing and the screen is turned 90 degrees,
 * the new screen will not have a handle to the media playing and it becomes
 * impossible to stop the recording from playing without force-closing the app.
 */
class MediaPlayerService (
    val mContext: Context,
) {

    private val mMediaPlayer = MediaPlayer()
    var playing by mutableStateOf(false)
        private set
    var paused by mutableStateOf(false)
        private set
    var stopped by mutableStateOf(true)

    val getIsPlaying: Boolean
        get() = playing

    val getIsPaused: Boolean
        get() = paused

    val getIsStopped: Boolean
        get() = stopped

    suspend fun play(filename: String) {
        Log.e("BEN4", filename)
        if (paused) {
            mMediaPlayer.start()
            playing = true
            paused = false
            stopped = false
            return
        }
        // if stopped
        mMediaPlayer.reset()
        mMediaPlayer.setDataSource(mContext.assets.openFd(filename))
        mMediaPlayer.prepare()
        playing = true
        paused = false
        stopped = false
        mMediaPlayer.start()
    }

    suspend fun pause() {
        mMediaPlayer.pause()
        playing = false
        paused = true
        stopped = false
    }

    /**
     * Regardless of the value of [initialProgress] the reset function will reset the
     * [currentProgress] to 0
     */
    fun stop(filename: String) {
        if (stopped) {
            return
        }

        playing = false
        paused = false
        stopped = true
        mMediaPlayer.stop()
        mMediaPlayer.prepare()
    }
}