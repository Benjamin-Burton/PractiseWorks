
package burt.music.practiseworks.ui.listScreens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontSynthesis.Companion.Weight
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import burt.music.practiseworks.data.PractiseSessionTypeNumCompleted
import burt.music.practiseworks.ui.navigation.NavigationDestination
import burt.music.practiseworks.ui.PractiseWorksTopAppBar
import burt.music.practiseworks.data.Task
import burt.music.practiseworks.ui.AppViewModelProvider
import burt.music.practiseworks.ui.home.HomeDestination
import burt.music.practiseworks.ui.item.ItemEditDestination
import burt.music.practiseworks.ui.task.TaskTypes
import burt.music.practiseworks.ui.theme.PractiseWorksTheme
import com.example.practiseworks.R
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

object PractiseScreenDestination : NavigationDestination {
    override val route = "practise_screen"
    override val titleRes = R.string.app_name
    const val practiseIdArg = "practiseId"
    val routeWithArgs = "$route/{$practiseIdArg}"
}

/**
 * Entry route for TaskList screen
 */
@Composable
fun PractiseScreen(
    onNavigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PractiseScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val practiseScreenUiState by viewModel.practiseScreenUiState.collectAsState()
    val practiseScreenTypesState by viewModel.practiseTypesUiState.collectAsState()
    val practiseScreenNumCompletedTasksUiState by viewModel.numCompletedTasksUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            PractiseWorksTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateBack
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.endSession()
                        onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
                ) {
                Text(
                    text = "End Practise Session",
                    fontSize = 20.sp
                )
            }
        }
    ) { innerPadding ->
        // what we need is a button for each task type,
        // and a number of those tasks
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedCard(
                elevation = CardDefaults.cardElevation(),
                colors = CardDefaults.cardColors(),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)) {

                    for (practiseType in practiseScreenTypesState.practiseTypes) {
                        TaskTypeButton(
                            taskType = TaskTypes.valueOf(practiseType.type.uppercase()),
                            numberInSession = practiseType.numTotal,
                            numberComplete = (practiseScreenNumCompletedTasksUiState.practiseList.firstOrNull() {it.type == practiseType.type}?.numCompleted
                                ?: 0),
                            onTaskTypeClick = {
                                navController.navigate("${TaskListDestination.route}/${viewModel.getPractiseSessionId()}/${practiseType.type}")
                            })
                        Spacer(Modifier.size(16.dp))
                    }
                    Spacer(Modifier.size(16.dp))

                }
            }
        }

//        var allDone = true
//        for (practiseType in practiseScreenTypesState.practiseTypes) {
//            if (practiseType.numTotal != practiseType.numCompleted) {
//                allDone = false
//                Log.e("BEN","here i sit")
//            }
//        }
//        if (allDone) {
//            PractiseCompleteConfirmationDialog(
//                onNavigateBack = onNavigateBack
//            )
//        }

//        TaskListBody(
//            taskList = practiseScreenUiState.taskList,
//            onItemClick = navigateToTaskUpdate,
//            modifier = modifier.padding(innerPadding)
//        )
    }
}

@Composable
private fun PractiseCompleteConfirmationDialog(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
) {
    Dialog(
        onDismissRequest = {
            onNavigateBack
        },
        content = {
            Text(
                text = "Well done, practise complete!"
            )
        }
    )
}

@Composable
private fun TaskTypeButton(
    taskType: TaskTypes,
    numberInSession: Int,
    numberComplete: Int,
    onTaskTypeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
            onClick = onTaskTypeClick,
            colors = ButtonDefaults.buttonColors(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "${taskType.string.replaceFirstChar { it.uppercaseChar() }}s ${numberComplete.toString()} / ${numberInSession.toString()}", color = Color.White,
                fontSize = 20.sp
            )
        }
//        PractiseWorksListHeader()
//        Divider()
//        if (taskList.isEmpty()) {
//            Text(
//                text = stringResource(R.string.no_tasks_description),
//                style = MaterialTheme.typography.subtitle2
//            )
//        } else {
//            PractiseWorksTaskList(taskList = taskList, onItemClick = { onItemClick(it.id) })
//        }
}
//
//@Composable
//private fun PractiseWorksTaskList(
//    taskList: List<Task>,
//    onItemClick: (Task) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
//        items(items = taskList, key = { it.id }) { task ->
//            PractiseWorksTask(task = task, onItemClick = onItemClick)
//            Divider()
//        }
//    }
//}
//
////@Composable
////private fun PractiseWorksListHeader(modifier: Modifier = Modifier) {
////    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
////        headerList.forEach {
////            Text(
////                text = stringResource(it.headerStringId),
////                modifier = Modifier.weight(it.weight),
////                style = MaterialTheme.typography.h6
////            )
////        }
////    }
////}
//
//@Composable
//private fun PractiseWorksTask(
//    task: Task,
//    onItemClick: (Task) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Card(
//        modifier = modifier.padding(8.dp),
//        elevation = 4.dp
//    ) {
//        Row(modifier = modifier
//            .fillMaxWidth()
//            .clickable { onItemClick(task) }
//            .padding(vertical = 16.dp)
//        ) {
//            taskIcon(R.drawable.task_icon,
//                completed = true)
//            Text(
//                text = task.title,
//                modifier = Modifier.weight(1.5f),
//                fontWeight = FontWeight.Bold
//            )
//        }
//    }
//}


@Preview(showBackground = true)
@Composable
fun PractiseScreenRoutePreview() {
    PractiseWorksTheme {
//        TaskTypes(
//            listOf(
//                Task(1, "", 100, TaskTypes.WARMUP.string, "Play it right", true, "Again: lower your wrist."),
//                Task(2, "Pen", 100, TaskTypes.WARMUP.string, "And close it", true, "And open it"),
//                Task(3, "TV", 100, TaskTypes.WARMUP.string, "C major scale.", true, "1,2,3,1,2,3,4,5")
//            ),
//            onItemClick = {}
//        )
    }
}
