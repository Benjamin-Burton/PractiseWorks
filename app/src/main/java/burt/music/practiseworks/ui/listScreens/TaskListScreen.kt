
package burt.music.practiseworks.ui.listScreens

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import burt.music.practiseworks.ui.navigation.NavigationDestination
import burt.music.practiseworks.ui.PractiseWorksTopAppBar
import burt.music.practiseworks.data.Task
import burt.music.practiseworks.data.TaskPlusCompleted
import burt.music.practiseworks.ui.AppViewModelProvider
import burt.music.practiseworks.ui.home.HomeDestination
import burt.music.practiseworks.ui.task.PieceDetailsDestination
import burt.music.practiseworks.ui.task.TaskTypes
import burt.music.practiseworks.ui.task.WarmupDetailsDestination
import burt.music.practiseworks.ui.theme.PractiseWorksTheme
import com.example.practiseworks.R

object TaskListDestination : NavigationDestination {
    override val route = "task_list"
    override val titleRes = R.string.app_name
    const val sessionIdArg = "sessionId"
    const val typeArg = "type"
    val routeWithArgs = "$route/{$sessionIdArg}/{$typeArg}"
}

/**
 * Entry route for TaskList screen
 */
@Composable
fun TaskListScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    viewModel: TaskListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val taskListUiState by viewModel.taskListUiState.collectAsState()
    Scaffold(
        topBar = {
            PractiseWorksTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateBack
            )
        },
    ) { innerPadding ->
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
                TaskListBody(
                    taskList = taskListUiState.taskList,
                    onItemClick = {
                        if (viewModel.getType() == "warmup") {
                            navController.navigate("${WarmupDetailsDestination.route}/${viewModel.getPractiseSessionId()}/$it")
                        } else if (viewModel.getType() == "exercise") {
                            // navController.navigate("${ExerciseDetailsDestination.route}/${viewModel.getPractiseSessionId()}/$it")
                        } else if (viewModel.getType() == "piece") {
                            // navigate to piece screen
                            navController.navigate("${PieceDetailsDestination.route}/${viewModel.getPractiseSessionId()}/$it")
                        }
                    },
                    modifier = modifier.padding(innerPadding)
                )
            }
        }
        viewModel.printLogs()
    }
}

@Composable
private fun TaskListBody(
    taskList: List<TaskPlusCompleted>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (taskList.isEmpty()) {
            PractiseWorksListHeader(taskType = "No tasks.")
        } else {
            PractiseWorksListHeader(taskType = taskList[0].type)
        }
        Divider()
        if (taskList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_tasks_description),
                style = MaterialTheme.typography.subtitle2
            )
        } else {
            PractiseWorksTaskList(taskList = taskList, onItemClick = { onItemClick(it.id) })
        }
    }
}

@Composable
private fun PractiseWorksTaskList(
    taskList: List<TaskPlusCompleted>,
    onItemClick: (TaskPlusCompleted) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items = taskList, key = { it.id }) { task ->
            PractiseWorksTask(task = task, onItemClick = onItemClick)
        }
    }
}

@Composable
private fun PractiseWorksListHeader(
    modifier: Modifier = Modifier,
    taskType: String) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        headerList.forEach {
            Text(
                text = taskType.replaceFirstChar {it.uppercase() } + " Tasks",
                modifier = Modifier.weight(it.weight),
                style = MaterialTheme.typography.h6
            )
        }
    }
}

@Composable
private fun PractiseWorksTask(
    task: TaskPlusCompleted,
    onItemClick: (TaskPlusCompleted) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(8.dp)
            .height(80.dp),
        elevation = 4.dp,
    ) {
        Row(modifier = modifier
            .fillMaxSize()
            .clickable { onItemClick(task) }
        ) {
            if (task.completed) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        taskIcon(
                            R.drawable.task_icon_complete,
                            task.completed
                        )
                    }
                }
                Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(4f)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = task.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        taskIcon(
                            R.drawable.task_icon,
                            task.completed
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(4f)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = task.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * Composable that displays a photo of a dog.
 *
 * @param dogIcon is the resource ID for the image of the dog
 * @param modifier modifiers to set to this composable
 */
@Composable
fun taskIcon(@DrawableRes taskIcon: Int,
             completed: Boolean,
             modifier: Modifier = Modifier) {
    Image(
        modifier = modifier
            .size(64.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(50)),
        contentScale = ContentScale.Crop,
        painter = painterResource(taskIcon),
        /*
         * Content Description is not needed here - image is decorative, and setting a null content
         * description allows accessibility services to skip this element during navigation.
         */
        contentDescription = null
    )
}

private data class PractiseWorksHeader(@StringRes val headerStringId: Int, val weight: Float)

private val headerList = listOf(
    PractiseWorksHeader(headerStringId = R.string.task_list_title, weight = 1.5f)
)

@Preview(showBackground = true)
@Composable
fun TaskListScreenRoutePreview() {
    PractiseWorksTheme {
        TaskListBody(
            listOf(
                TaskPlusCompleted(1, "", 100, TaskTypes.WARMUP.string, "Play it right", true, "Again: lower your wrist.", true, "exercise_1_ng.mp3"),
                TaskPlusCompleted(2, "Pen", 100, TaskTypes.WARMUP.string, "And close it", true, "And open it", false, "exercise_2_5_note_scale.mp3"),
                TaskPlusCompleted(3, "TV", 100, TaskTypes.WARMUP.string, "C major scale.", true, "1,2,3,1,2,3,4,5", true, "exercise_4_scale_in_3rds.mp3")
            ),
            onItemClick = {}
        )
    }
}
