package burt.music.practiseworks.mediaService

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.practiseworks.R
import kotlinx.coroutines.delay

class MetronomeService(
    var bpm: Int = 96,
    val maxBpm: Int = 220,
    val minBpm: Int = 30,
    var beatDiv: Int = 4,
    val mContext: Context,
    var playing: Boolean = false
) {
    val mMediaPlayer = MediaPlayer.create(mContext, R.raw.click)
    var currentBpm by mutableStateOf(bpm)
        private set
    init {
        require(bpm >= 30) { "bpm=$bpm; must be >= 30" }
        require(bpm <= 220) { "bpm=$bpm; must be <= 220" }
    }

    suspend fun play() {
        playing = true
        while (playing) {
            delay((60000L / bpm))
            mMediaPlayer.stop()
            mMediaPlayer.prepare()
            println("tick")
            mMediaPlayer.start()
        }
    }

    /**
     * Regardless of the value of [initialProgress] the reset function will reset the
     * [currentProgress] to 0
     */
    fun reset() {
        playing = false
    }

    fun increaseBpm(amount: Int) {
        currentBpm += amount
        if (currentBpm >= maxBpm) { currentBpm = maxBpm }
    }

    fun decreaseBpm(amount: Int) {
        currentBpm -= amount
        if (currentBpm <= minBpm) { currentBpm = minBpm }
    }

    val getCurrentBpm: Int
        get() = currentBpm
}