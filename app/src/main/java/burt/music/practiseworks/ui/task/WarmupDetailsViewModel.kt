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

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import burt.music.practiseworks.data.*
import burt.music.practiseworks.mediaService.MetronomeService
import burt.music.practiseworks.ui.item.ItemDetailsDestination
import kotlinx.coroutines.flow.*

/**
 * ViewModel to retrieve, update and delete a task from the data source.
 */
class WarmupDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val tasksRepository: TasksRepository,
    private val practiseSessionTasksRepository: PractiseSessionTasksRepository,
    private val studentsRepository: StudentsRepository
) : ViewModel() {

    private val taskId: Int = checkNotNull(savedStateHandle[WarmupDetailsDestination.taskIdArg])
    private val sessionId: Int = checkNotNull(savedStateHandle[WarmupDetailsDestination.sessionArg])
    val uiState: StateFlow<WarmupUiState> =
        tasksRepository.getTaskStream(taskId)
            .filterNotNull()
            .map {
                it.toWarmupUiState()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = WarmupUiState()
            )
    // use this to get whether the task has been completed or not
    val uiStatePractiseSessionTask: StateFlow<PractiseSessionTaskUiState> =
        practiseSessionTasksRepository.getPracticeSessionTaskBySessionIdAndTaskId(sessionId, taskId)
            .filterNotNull()
            .map {
                it.toPractiseSessionTaskUiState()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = PractiseSessionTaskUiState()
            )



    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    suspend fun completeItem() {
        val task = practiseSessionTasksRepository.getPracticeSessionTaskBySessionIdAndTaskId(sessionId, taskId).first()
        practiseSessionTasksRepository.updatePractiseSessionTask(
            PractiseSessionTask(
                practise_session_id = sessionId,
                task_id = taskId,
                type = task.type,
                completed = true
            )
        )
        var student: Student = studentsRepository.getStudentStream(1).first()
        student.total_points += 100
        studentsRepository.updateStudent(student)
    }
}
