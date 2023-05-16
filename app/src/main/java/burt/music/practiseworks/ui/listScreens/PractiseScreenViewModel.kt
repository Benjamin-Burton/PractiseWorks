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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import burt.music.practiseworks.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

/**
 * View Model to retrieve all items in the Room database.
 */
class PractiseScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val tasksRepository: TasksRepository,
    private val practiseSessionsRepository: PractiseSessionsRepository,
    private val practiseSessionTasksRepository: PractiseSessionTasksRepository) : ViewModel() {

    private val practiseSessionId: Int = checkNotNull(savedStateHandle[PractiseScreenDestination.practiseIdArg])
    val practiseScreenUiState: StateFlow<PractiseScreenUiState> =
        practiseSessionTasksRepository.getPractiseSessionTasksBySessionIdStream(practiseSessionId).map { PractiseScreenUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = PractiseScreenUiState()
            )
    val practiseTypesUiState: StateFlow<PractiseScreenTypesState> =
        practiseSessionTasksRepository.getNumTasksCompletedInfo(practiseSessionId).map {PractiseScreenTypesState(it)}
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = PractiseScreenTypesState()
            )

    val numCompletedTasksUiState: StateFlow<NumCompletedTasksUiState> =
        practiseSessionTasksRepository.getNumCompletedTasksBySessionIdAndType(practiseSessionId).map { NumCompletedTasksUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = NumCompletedTasksUiState()
            )
    fun getNumComplete(practiseType: String): Int {
        return tasksRepository.getNumTasksByType(practiseType)
    }

    fun getPractiseSessionId(): Int {
        return practiseSessionId
    }

    fun getNumInSession(practiseType: String): Int {
        return tasksRepository.getNumTasksByType(practiseType)
    }

    suspend fun endSession() {
        var ps = practiseSessionsRepository.getPractiseSessionStream(practiseSessionId).first()
        practiseSessionsRepository.updatePractiseSession(
            PractiseSession(
                id = ps.id,
                student_id = ps.student_id,
                start_date = ps.start_date,
                end_date = Date()
            )
        )
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}

private fun getTypes() {

}

data class PractiseScreenTypesState(
    val practiseTypes: List<PractiseSessionCountInfo> = listOf()
)

/**
 * Ui State for PractiseScreenScreen
 */
data class PractiseScreenUiState(
    val practiseScreen: List<PractiseSessionTask> = listOf()
)

data class NumCompletedTasksUiState(
    val practiseList: List<PractiseSessionTypeNumCompleted> = listOf()
)
