/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package burt.music.practiseworks.ui.task

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.Help
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import burt.music.practiseworks.ui.AppViewModelProvider
import burt.music.practiseworks.ui.PractiseWorksTopAppBar
import burt.music.practiseworks.ui.item.ItemEntryBody
import burt.music.practiseworks.ui.item.ItemEntryDestination
import burt.music.practiseworks.ui.item.ItemUiState
import burt.music.practiseworks.ui.navigation.NavigationDestination
import burt.music.practiseworks.ui.theme.PractiseWorksTheme
import com.example.practiseworks.R
import kotlinx.coroutines.launch
import java.util.*

object TaskEntryDestination : NavigationDestination {
    override val route = "task_entry"
    override val titleRes = R.string.task_entry_title
}

@Composable
fun TaskEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,
    viewModel: TaskEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            PractiseWorksTopAppBar(
                title = stringResource(ItemEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        TaskEntryBody(
            taskUiState = viewModel.taskUiState,
            onTaskValueChange = viewModel::updateUiState,
            onSaveClick = { coroutineScope.launch {
                viewModel.saveTask()
            }},
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun TaskEntryBody(
    taskUiState: TaskUiState,
    onTaskValueChange: (TaskUiState) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        TaskInputForm(taskUiState = taskUiState, onValueChange = onTaskValueChange)
        Button(
            onClick = onSaveClick,
            enabled = taskUiState.actionEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_action))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskInputForm(
    taskUiState: TaskUiState,
    modifier: Modifier = Modifier,
    onValueChange: (TaskUiState) -> Unit = {},
    enabled: Boolean = true
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = taskUiState.title,
            onValueChange = { onValueChange(taskUiState.copy(title = it)) },
            label = { Text(stringResource(R.string.task_title_req)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = taskUiState.instructions,
            onValueChange = { onValueChange(taskUiState.copy(instructions = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = { Text(stringResource(R.string.task_instructions_req)) },
            modifier = Modifier.fillMaxWidth().height(100.dp),
            enabled = enabled,
            singleLine = false
        )
        OutlinedTextField(
            value = taskUiState.cues,
            onValueChange = { onValueChange(taskUiState.copy(cues = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = { Text(stringResource(R.string.task_cues_req)) },
            modifier = Modifier.fillMaxWidth().height(100.dp),
            enabled = enabled,
            singleLine = true
        )
    }

    var correctList : ArrayList<String> = ArrayList<String>()
    TaskTypes.values().forEach {
        correctList.add(it.string.replaceFirstChar {it.uppercase(Locale.getDefault())})
    }
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        modifier = Modifier.fillMaxWidth(),
        onExpandedChange = { isExpanded = it },

        ) {
        TextField(
            value = taskUiState.type.string.replaceFirstChar { it.uppercase(Locale.getDefault())},
            onValueChange = { },
            readOnly = true,
            label = { androidx.compose.material3.Text(text = "Task Type") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false })
        {
            correctList.forEach { selectedOption ->
                DropdownMenuItem(onClick = {
                    onValueChange(taskUiState.copy(type = TaskTypes.valueOf(selectedOption.uppercase())))
                    isExpanded = false
                }) {
                    androidx.compose.material3.Text(text = selectedOption)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskEntryScreenPreview() {
    PractiseWorksTheme {
        ItemEntryBody(
            itemUiState = ItemUiState(
                name = "Item name",
                price = "10.00",
                quantity = "5"
            ),
            onItemValueChange = {},
            onSaveClick = {}
        )
    }
}
