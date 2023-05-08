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

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import burt.music.practiseworks.data.Task
import burt.music.practiseworks.data.TasksRepository
import burt.music.practiseworks.ui.task.TaskUiState
import burt.music.practiseworks.ui.task.isValid
import java.util.logging.Logger

/**
 * View Model to validate and insert tasks in the Room database.
 */
class TaskEntryViewModel(private val tasksRepository: TasksRepository) : ViewModel() {

    /**
     * Holds current task ui state
     */
    var taskUiState by mutableStateOf(TaskUiState())
        private set

    /**
     * Updates the [taskUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(newTaskUiState: TaskUiState) {
        taskUiState = newTaskUiState.copy( actionEnabled = newTaskUiState.isValid())
    }

    suspend fun saveTask() {
        if (taskUiState.isValid()) {
            var toSave: Task = taskUiState.toTask()
            toSave.student_id = 1
            Log.i("FROM THE BOSS", toSave.student_id.toString())
            tasksRepository.insertTask(toSave)
        }
    }
}
