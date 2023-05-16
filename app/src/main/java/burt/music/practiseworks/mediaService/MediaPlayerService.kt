package burt.music.practiseworks.mediaService

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import com.example.practiseworks.R
import kotlinx.coroutines.delay

class MediaPlayerService (
    val filename: String = "",
    val mContext: Context,
) {
    val mMediaPlayer = MediaPlayer.create(mContext, R.raw.exercise_1_ng)
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
    suspend fun play() {
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
    fun stop() {
        playing = false
        paused = false
        stopped = true
        mMediaPlayer.stop()
        mMediaPlayer.prepare()
    }
}