
package burt.music.practiseworks.ui.welcome

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontSynthesis.Companion.Weight
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import burt.music.practiseworks.ui.navigation.NavigationDestination
import burt.music.practiseworks.ui.PractiseWorksTopAppBar
import burt.music.practiseworks.data.Item
import burt.music.practiseworks.data.PractiseSession
import burt.music.practiseworks.data.Student
import burt.music.practiseworks.ui.AppViewModelProvider
import burt.music.practiseworks.ui.home.HomeViewModel
import burt.music.practiseworks.ui.listScreens.PractiseScreenDestination
import burt.music.practiseworks.ui.theme.PractiseWorksTheme
import com.example.practiseworks.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants.FontStyle
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.text.NumberFormat
import java.util.*

object WelcomeDestination : NavigationDestination {
    override val route = "welcome"
    override val titleRes = R.string.app_name
}

/**
 * Entry route for Welcome screen
 */
@Composable
fun WelcomeScreen(
    navigateToFirstEntry: () -> Unit,
    navigateToItemUpdate: (Int) -> Unit,
    navigateToNewPractiseSession: (Int) -> Unit, // (Int)
    modifier: Modifier = Modifier,
    viewModel: WelcomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val welcomeUiState by viewModel.welcomeUiState.collectAsState()

    Scaffold(
        topBar = {
            PractiseWorksTopAppBar(
                title = stringResource(WelcomeDestination.titleRes),
                canNavigateBack = false
            )
        },
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = navigateToFirstEntry,
//                modifier = Modifier.navigationBarsPadding()
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Add,
//                    contentDescription = stringResource(R.string.item_entry_title),
//                    tint = MaterialTheme.colors.onPrimary
//                )
//            }
//        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .border(BorderStroke(2.dp, Color.Gray))
                .fillMaxSize()
        ) {
            Box(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.TopCenter
            ) {
                WelcomeProfile(
                    student = welcomeUiState.student,
                    onPictureClick = { },
                    onStatsClick = { },
                    onNewPractiseClick = { },
                )
            }
            Box(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.TopCenter
            ) {
                WelcomeButtons(
                    onNewPractiseClick = {
                        coroutineScope.launch {
                            viewModel.createNewPractiseSession()
                            Log.e("from Ben: ", "New practise seesion id: ${viewModel.newSessionId}")
                            navigateToNewPractiseSession(viewModel.getNewSessionIdAsInt())
                        }
                    },
                    onGenerateTasks = {
                        coroutineScope.launch {
                            viewModel.createTasks()
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun WelcomeProfile(
    student: Student,
    onPictureClick: () -> Unit,
    onStatsClick: () -> Unit,
    onNewPractiseClick: () -> Unit,
    modifier: Modifier = Modifier

) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .border(BorderStroke(2.dp, Color.Black))
            .background(Color.hsl(206f, 0f, 0.86f, 1f))
    ) {
        Box(
            modifier = modifier
                .padding(10.dp)
                .weight(1f)
        ) {
            profilePictureDisplay(
                profilePic = R.drawable.ben_profile,
                onPictureClick = { }
            )
        }
        Box(
            modifier = modifier
                .padding(10.dp)
                .weight(1f)
        ) {
            statisticsDisplay(
                student,
                onNewPractiseClick = { }
            )
        }

    }
}

@Composable
private fun profilePictureDisplay(
    modifier: Modifier = Modifier,
    @DrawableRes profilePic: Int,
    onPictureClick: () -> Unit
) {
    Image(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
            .clip(RoundedCornerShape(50)),
        contentScale = ContentScale.Fit,
        painter = painterResource(profilePic),
        /*
         * Content Description is not needed here - image is decorative, and setting a null content
         * description allows accessibility services to skip this element during navigation.
         */
        contentDescription = null
    )
}

@Composable
private fun statisticsDisplay(
    student: Student,
    modifier: Modifier = Modifier,
    onNewPractiseClick: () -> Unit
) {
    OutlinedCard(
        elevation = CardDefaults.cardElevation(),
        colors = CardDefaults.cardColors(),
        modifier = Modifier.fillMaxSize()
            .padding(5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val padding = 10.dp
            Text(
                text = "Name",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Divider()
            Row() {
                Text(
                    text = student.f_name + " " + student.l_name,
                    fontSize = 14.sp
                )
            }
            Spacer(Modifier.size(padding))
            Text(
                text = "Username: ",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Divider()
            Row() {
                Text(
                    text = student.username,
                    fontSize = 14.sp
                )
            }
            Spacer(Modifier.size(padding))
            Text(
                text = "Lesson Details:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Divider()
            Text(
                text = student.lesson_day + " " + student.lesson_time,
                fontSize = 14.sp
            )
            Spacer(Modifier.size(padding))
            Text(
                text = "Practise Goal:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Divider()
            Text(
                text = student.practise_goal.toString() + " times per week",
                fontSize = 14.sp
            )
            Spacer(Modifier.size(padding))
            Text(
                text = "Total Points:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Divider()
            Text(
                text = student.total_points.toString(),
                fontSize = 14.sp
            )
            Spacer(Modifier.size(padding))
        }
    }
}

@Composable
private fun WelcomeButtons(
    modifier: Modifier = Modifier,
    onNewPractiseClick: () -> Unit,
    onGenerateTasks: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedCard(
            elevation = CardDefaults.cardElevation(),
            colors = CardDefaults.cardColors()
        ) {
            val padding = 24.dp
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onNewPractiseClick,
                    colors = ButtonDefaults.buttonColors()

                ) {
                    Text(
                        text = "Start New Practise!",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
                Spacer(Modifier.size(padding))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(),
                ) {
                    Text(
                        text = "Explore",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
                Spacer(Modifier.size(padding))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onGenerateTasks,
                    colors = ButtonDefaults.buttonColors(),
                ) {
                    Text(
                        text = "Generate Tasks in Database",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
            }

        }

    }
}

@Composable
private fun WelcomeBody(
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        InventoryListHeader()
        Divider()
        if (true) {
            Text(
                text = stringResource(R.string.no_item_description),
                style = MaterialTheme.typography.subtitle2
            )
        } else {

        }
    }
}

@Composable
private fun InventoryList(
    itemList: List<Item>,
    onItemClick: (Item) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items = itemList, key = { it.id }) { item ->
            InventoryItem(item = item, onItemClick = onItemClick)
            Divider()
        }
    }
}

@Composable
private fun InventoryListHeader(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        headerList.forEach {
            Text(
                text = stringResource(it.headerStringId),
                modifier = Modifier.weight(it.weight),
                style = MaterialTheme.typography.h6
            )
        }
    }
}

@Composable
private fun InventoryItem(
    item: Item,
    onItemClick: (Item) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable { onItemClick(item) }
        .padding(vertical = 16.dp)
    ) {
        Text(
            text = item.name,
            modifier = Modifier.weight(1.5f),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = NumberFormat.getCurrencyInstance().format(item.price),
            modifier = Modifier.weight(1.0f)
        )
        Text(text = item.quantity.toString(), modifier = Modifier.weight(1.0f))
    }
}

private data class InventoryHeader(@StringRes val headerStringId: Int, val weight: Float)

private val headerList = listOf(
    InventoryHeader(headerStringId = R.string.item, weight = 1.5f),
    InventoryHeader(headerStringId = R.string.price, weight = 1.0f),
    InventoryHeader(headerStringId = R.string.quantity_in_stock, weight = 1.0f)
)

@Preview(showBackground = true)
@Composable
fun WelcomeProfileRoutePreview() {
    PractiseWorksTheme {
        WelcomeProfile(
            student = Student(0, "test", "test", "test", "test", "test", 1, Date(), "test", "12:00", practise_goal = 4, total_points = 4000),
            onPictureClick = { },
            onStatsClick = { },
            onNewPractiseClick = { }
        )
    }
}