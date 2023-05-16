package burt.music.practiseworks.ui.task

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
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import burt.music.practiseworks.ui.AppViewModelProvider
import burt.music.practiseworks.ui.navigation.NavigationDestination
import burt.music.practiseworks.ui.PractiseWorksTopAppBar
import burt.music.practiseworks.ui.item.ItemInputForm
import burt.music.practiseworks.ui.listScreens.PractiseScreenViewModel
import burt.music.practiseworks.ui.task.WarmupUiState
import burt.music.practiseworks.ui.theme.PractiseWorksTheme
import com.example.practiseworks.R
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
    val warmUpUiState by viewModel.uiState.collectAsState()
    val practiseSessionTaskUiState by viewModel.uiStatePractiseSessionTask.collectAsState()
    Scaffold(
        topBar = {
            PractiseWorksTopAppBar(
                title = stringResource(WarmupDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },

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