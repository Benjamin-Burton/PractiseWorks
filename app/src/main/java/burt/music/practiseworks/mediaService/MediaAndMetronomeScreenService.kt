package burt.music.practiseworks.mediaService

import android.content.Context
import android.provider.MediaStore.Audio.Media
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

/**
 * A combination of the metronome and media player into one composable.
 * This is not implemented and not used, yet.
 */
class MediaAndMetronomeScreenService {
    @Composable
    fun MetronomeView (
        metronome: MetronomeService,
        cont: Context,
    ) {
        val coroutineScope = rememberCoroutineScope()
        // val player = MediaPlayerService()

    }
}