package burt.music.practiseworks.ui.task

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import burt.music.practiseworks.mediaService.MediaPlayerService
import burt.music.practiseworks.mediaService.MetronomeService
import burt.music.practiseworks.ui.AppViewModelProvider
import burt.music.practiseworks.ui.navigation.NavigationDestination
import burt.music.practiseworks.ui.PractiseWorksTopAppBar
import burt.music.practiseworks.ui.theme.PractiseWorksTheme
import com.example.practiseworks.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object PieceDetailsDestination : NavigationDestination {
    override val route = "piece_details"
    override val titleRes = R.string.piece_detail_title
    const val taskIdArg = "taskId"
    const val sessionArg = "sessionArg"
    val routeWithArgs = "$route/{$sessionArg}/{$taskIdArg}"
}

@Composable
fun PieceDetailsScreen(
    navigateBack: () -> Unit,
    onCompleteWarmup: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PieceDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val pieceUiState by viewModel.uiState.collectAsState()
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
        mediaPlayer.stop(filename = pieceUiState.track_filename)
        metronome.reset()
        navigateBack()
    }

    Scaffold(
        topBar = {
            PractiseWorksTopAppBar(
                title = "Piece Details",
                canNavigateBack = true,
                navigateUp = {
                    mediaPlayer.stop(filename = pieceUiState.track_filename)
                    navigateBack()
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
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
                            mediaPlayer.stop(filename = pieceUiState.track_filename)
                            metronomeOrMedia = !metronomeOrMedia
                        },
                        filename = pieceUiState.track_filename
                    )
                }
            }
        }

    ) { innerPadding ->
        Log.e("BEN4", pieceUiState.track_filename)
        PieceDetailsBody(
            pieceUiState = pieceUiState,
            practiseSessionTaskUiState = practiseSessionTaskUiState,
            mediaPlayer = mediaPlayer,
            onCompleteWarmup = {
                               coroutineScope.launch {
                                   viewModel.completeItem()
                                   navigateBack()
                               }
            },
            navigateBack = {
                mediaPlayer.stop(filename = pieceUiState.track_filename)
                navigateBack()
            },
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun PieceDetailsBody(
    pieceUiState: PieceUiState,
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
        PieceDetailsForm(pieceUiState = pieceUiState)
        Button(
            onClick = onCompleteWarmup,
            modifier = Modifier.fillMaxWidth(),
            enabled = true
        ) {
            Text("Completed")
        }
        OutlinedButton(
            onClick = {
                mediaPlayer.stop(filename = pieceUiState.track_filename)
                navigateBack()
            } ,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go Back")
        }
    }
}

@Composable
private fun PieceDetailsForm(
    pieceUiState: PieceUiState
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
                        text = pieceUiState.title,
                        style = MaterialTheme.typography.h6,
                    )
                }
            }
            Spacer(Modifier.size(20.dp))
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
                        text = pieceUiState.instructions,
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
                    text = pieceUiState.cues,
                    style = MaterialTheme.typography.body2
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PieceDetailsScreenPreview() {
    PractiseWorksTheme {
        PieceDetailsBody(
            pieceUiState = PieceUiState(
                title = "Piece name",
                instructions = "A lovely song.",
                cues = "A lovely tune. A dodgy accompaniment."),
            practiseSessionTaskUiState = PractiseSessionTaskUiState(
                4,
                3,
                "piece",
                true,
            ),
            onCompleteWarmup = {},
            navigateBack = {},
            mediaPlayer = MediaPlayerService(LocalContext.current),
        )
    }
}