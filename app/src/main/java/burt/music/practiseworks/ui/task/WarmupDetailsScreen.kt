package burt.music.practiseworks.ui.task

import android.content.Context
import android.media.DrmInitData.SchemeInitData
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalMapOf
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
    override val titleRes = R.string.warmup_detail_title
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
        mutableStateOf(false) // true = metronome, false = media player
    }

    val metronome by remember { mutableStateOf(MetronomeService(
            mContext = context
        ))
    }

    val mediaPlayer by remember {
        mutableStateOf(MediaPlayerService(
            mContext = context,
        ))
    }
    BackHandler {
        mediaPlayer.stop(filename = warmUpUiState.track_filename)
        metronome.reset()
        navigateBack()
    }

    Scaffold(
        topBar = {
            PractiseWorksTopAppBar(
                title = stringResource(WarmupDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = {
                    mediaPlayer.stop(filename = warmUpUiState.track_filename)
                    navigateBack()
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                if (metronomeOrMedia) {
                    MetronomeUiComponent(
                        metronome = metronome,
                        coroutineScope = coroutineScope,
                        onChange = {
                            metronome.reset()
                            metronomeOrMedia = !metronomeOrMedia
                        }
                    )
                } else {
                    MediaPlayerUiComponent(
                        mediaPlayer,
                        coroutineScope,
                        onChange = {
                            mediaPlayer.stop(filename = warmUpUiState.track_filename)
                            metronomeOrMedia = !metronomeOrMedia
                        },
                        filename = warmUpUiState.track_filename
                    )
                }
            }
        }


    ) { innerPadding ->
        Log.e("BEN4", warmUpUiState.track_filename)
        WarmupDetailsBody(
            warmupUiState = warmUpUiState,
            practiseSessionTaskUiState = practiseSessionTaskUiState,
            mediaPlayer = mediaPlayer,
            onCompleteWarmup = {
                               coroutineScope.launch {
                                   viewModel.completeItem()
                                   navigateBack()
                               }
            },
            navigateBack = {
                mediaPlayer.stop(filename = warmUpUiState.track_filename)
                navigateBack()
            },
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun MediaPlayerUiComponent(
    mediaPlayer: MediaPlayerService,
    coroutineScope: CoroutineScope,
    onChange: () -> Unit,
    filename: String,
) {
    Divider()
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            modifier = Modifier.padding(3.dp),
                            onClick = {
                                mediaPlayer.stop(filename = filename)
                            }
                        ) {
                            Text(
                                text = "Stop",
                                fontSize = 12.sp
                            )
                        }
                        Button(
                            modifier = Modifier.padding(3.dp),
                            onClick = {
                                if (mediaPlayer.getIsStopped) {
                                    coroutineScope.launch {
                                        mediaPlayer.play(filename)
                                    }
                                } else if (mediaPlayer.getIsPaused) {
                                    coroutineScope.launch {
                                        mediaPlayer.play(filename)
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
                                    text = "Play ",
                                    fontSize = 12.sp
                                )
                            } else {
                                Text(
                                    text = if (mediaPlayer.getIsPaused) "Play " else "Pause",
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Button(
                        modifier = Modifier.padding(3.dp),
                        onClick = onChange
                    ) {
                        Text(
                            text = ">"
                        )
                    }
                }
            }
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(3.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = filename,
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
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
    Divider()
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(4f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    modifier = Modifier.padding(2.dp),
                    onClick = {
                        metronome.reset()
                    }
                ) {
                    Text(
                        text = "Stop"
                    )
                }
                Button(
                    modifier = Modifier.padding(2.dp),
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
                    modifier = Modifier.padding(2.dp),
                    onClick = {
                        coroutineScope.launch {
                            metronome.increaseBpm(4)
                        }
                    }
                ) {
                    Text(
                        text = " + "
                    )
                }
                Button(
                    modifier = Modifier.padding(2.dp),
                    onClick = {
                        coroutineScope.launch {
                            metronome.decreaseBpm(4)
                        }
                    }
                ) {
                    Text(
                        text = " - "
                    )
                }
                Button(
                    modifier = Modifier.padding(2.dp),
                    onClick = onChange
                ) {
                    Text(
                        text = " > "
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "BPM: " + metronome.getCurrentBpm.toString(),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun WarmupDetailsBody(
    warmupUiState: WarmupUiState,
    practiseSessionTaskUiState: PractiseSessionTaskUiState,
    onCompleteWarmup: () -> Unit,
    navigateBack: () -> Unit,
    mediaPlayer: MediaPlayerService,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        WarmupDetailsForm(warmupUiState = warmupUiState)
        Button(
            onClick = onCompleteWarmup,
            modifier = Modifier.fillMaxWidth(),
            enabled = true
        ) {
            Text("Completed")
        }
        OutlinedButton(
            onClick = {
                mediaPlayer.stop(filename = warmupUiState.track_filename)
                navigateBack()
            } ,
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
            OutlinedCard(
                elevation = CardDefaults.cardElevation(),
                colors = CardDefaults.cardColors(),
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Instructions",
                        style = MaterialTheme.typography.h6,

                    )
                }
                Divider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = warmupUiState.instructions,
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        Spacer(modifier = Modifier.padding(16.dp))
        OutlinedCard(
            elevation = CardDefaults.cardElevation(),
            colors = CardDefaults.cardColors(),
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Cues",
                    style = MaterialTheme.typography.h6,
                )
            }
            Divider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = warmupUiState.cues,
                    style = MaterialTheme.typography.body2
                )
            }

        }
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
            navigateBack = {},
            mediaPlayer = MediaPlayerService(LocalContext.current),
        )
    }
}
