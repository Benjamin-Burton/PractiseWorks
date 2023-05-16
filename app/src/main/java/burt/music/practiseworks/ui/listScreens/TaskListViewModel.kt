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

package burt.music.practiseworks.ui.listScreens

import TaskListUiState
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import burt.music.practiseworks.data.Task
import burt.music.practiseworks.data.TasksRepository
import burt.music.practiseworks.ui.task.TaskTypes
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * View Model to retrieve all items in the Room database.
 */
class TaskListViewModel(
    savedStateHandle: SavedStateHandle,
    tasksRepository: TasksRepository) : ViewModel() {
    private val practiseSessionId: Int = checkNotNull(savedStateHandle[TaskListDestination.sessionIdArg])
    private val type: String = checkNotNull(savedStateHandle[TaskListDestination.typeArg])


    val taskListUiState: StateFlow<TaskListUiState> =
        tasksRepository.getTasksPlusCompletedByTypeAndSessionId(practiseSessionId, type).map { TaskListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = TaskListUiState()
            )
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    fun printLogs() {
        Log.e("BEN2", practiseSessionId.toString())
        Log.e("BEN2", type)
    }

    fun getType(): String {
        return type
    }

    fun getPractiseSessionId(): Int {
        return practiseSessionId
    }
}

/**
 * Ui State for TaskListScreen
 */
