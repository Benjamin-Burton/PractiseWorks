package burt.music.practiseworks.ui.task

import android.content.Context
import android.media.DrmInitData.SchemeInitData
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import burt.music.practiseworks.mediaService.MediaAndMetronomeScreenService
import burt.music.practiseworks.mediaService.MediaPlayerService
import burt.music.practiseworks.mediaService.MetronomeService
import burt.music.practiseworks.ui.AppViewModelProvider
import burt.music.practiseworks.ui.navigation.NavigationDestination
import burt.music.practiseworks.ui.PractiseWorksTopAppBar
import burt.music.practiseworks.ui.item.ItemInputForm
import burt.music.practiseworks.ui.listScreens.PractiseScreenViewModel
import burt.music.practiseworks.ui.task.WarmupUiState
import burt.music.practiseworks.ui.theme.PractiseWorksTheme
import com.example.practiseworks.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

object WarmupDetailsDestination : NavigationDestination {
    override val route = "warmup_details"
    override val titleRes = R.string.item_detail_title
    const val taskIdArg = "taskId"
    const val sessionArg = "sessionArg"
    val routeWithArgs = "$route/{$sessionArg}/{$taskIdArg}"
}

@Composable
fun WarmupDetailsScreen(
    navigateBack: () -> Unit,
    onCompleteWarmup: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WarmupDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val warmUpUiState by viewModel.uiState.collectAsState()
    val practiseSessionTaskUiState by viewModel.uiStatePractiseSessionTask.collectAsState()
    var metronomeOrMedia by remember {
        mutableStateOf(true) // true = metronome
    }

    val metronome by remember { mutableStateOf(MetronomeService(
            mContext = context
        ))
    }
    val mediaPlayer by remember {
        mutableStateOf(MediaPlayerService(
            mContext = context,
            filename = "exercise_1_ng.mp3"
        ))
    }
    Scaffold(
        topBar = {
            PractiseWorksTopAppBar(
                title = stringResource(WarmupDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (metronomeOrMedia) {
                    MetronomeUiComponent(
                        metronome = metronome,
                        coroutineScope = coroutineScope,
                        onChange = {
                            metronomeOrMedia = !metronomeOrMedia
                        }
                    )
                } else {
                    MediaPlayerUiComponent(
                        mediaPlayer,
                        coroutineScope,
                        onChange = {
                            metronomeOrMedia = !metronomeOrMedia
                        },
                    )
                }
            }
        }


    ) { innerPadding ->
        WarmupDetailsBody(
            warmupUiState = warmUpUiState,
            practiseSessionTaskUiState = practiseSessionTaskUiState,
            onCompleteWarmup = {
                               coroutineScope.launch {
                                   viewModel.completeItem()
                                   navigateBack()
                               }
            },
            navigateBack = navigateBack,
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun MediaPlayerUiComponent(
    mediaPlayer: MediaPlayerService,
    coroutineScope: CoroutineScope,
    onChange: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        mediaPlayer.stop()
                    }
                ) {
                    Text(
                        text = "Stop"
                    )
                }
                Button(
                    onClick = {
                        if (mediaPlayer.getIsStopped) {
                            coroutineScope.launch {
                                mediaPlayer.play()
                            }
                        } else if (mediaPlayer.getIsPaused) {
                            coroutineScope.launch {
                                mediaPlayer.play()
                            }
                        } else {
                            coroutineScope.launch {
                                mediaPlayer.pause()
                            }
                        }
                    }
                ) {
                    if (mediaPlayer.getIsStopped) {
                        Text(
                            text = "Play"
                        )
                    } else {
                        Text(
                            text = if (mediaPlayer.getIsPaused) "Play" else "Pause"
                        )
                    }
                }
                Button(
                    onClick = onChange
                ) {
                    Text(
                        text = "   >   "
                    )
                }
            }
        }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = mediaPlayer.filename,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun MetronomeUiComponent(
    metronome: MetronomeService,
    coroutineScope: CoroutineScope,
    onChange: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = {
                metronome.reset()
            }
        ) {
            Text(
                text = "Stop"
            )
        }
        Button(
            onClick = {
                coroutineScope.launch {
                    metronome.play()
                }
            },
        ) {
            Text(
                text = "Start"
            )
        }
        Button(
            onClick = {
                coroutineScope.launch {
                    metronome.increaseBpm(4)
                }
            }
        ) {
            Text(
                text = "   +   "
            )
        }
        Button(
            onClick = {
                coroutineScope.launch {
                    metronome.decreaseBpm(4)
                }
            }
        ) {
            Text(
                text = "   -   "
            )
        }
        Button(
            onClick = onChange
        ) {
            Text(
                text = "   >   "
            )
        }
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = metronome.getCurrentBpm.toString(),
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

}

@Composable
private fun WarmupDetailsBody(
    warmupUiState: WarmupUiState,
    practiseSessionTaskUiState: PractiseSessionTaskUiState,
    onCompleteWarmup: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
        WarmupDetailsForm(warmupUiState = warmupUiState)
        Button(
            onClick = onCompleteWarmup,
            modifier = Modifier.fillMaxWidth(),
            enabled = true
        ) {
            Text("Completed")
        }
        OutlinedButton(
            onClick = navigateBack ,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go Back")
        }
    }
}

@Composable
private fun WarmupDetailsForm(
    warmupUiState: WarmupUiState
) {
    Log.e("BEN3", warmupUiState.instructions)
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
            Text(
                text = "INSTRUCTIONS"
            )
            Text(
                text = warmupUiState.instructions
            )

            Text(
                text = "CUES"
            )
            Text(
                text = warmupUiState.cues
            )
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier.padding(16.dp),
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = stringResource(R.string.yes))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun WarmupDetailsScreenPreview() {
    PractiseWorksTheme {
        WarmupDetailsBody(
            warmupUiState = WarmupUiState(
                title = "Warmup name",
                instructions = "Please play well.",
                cues = "Remember to keep your eyes open."),
            practiseSessionTaskUiState = PractiseSessionTaskUiState(
                4,
                3,
                "warmup",
                true,
            ),
            onCompleteWarmup = {},
            navigateBack = {}
        )
    }
}
