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

package burt.music.practiseworks.ui.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import burt.music.practiseworks.data.*
import burt.music.practiseworks.ui.task.TaskTypes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

/**
 * View Model to retrieve all items in the Room database.
 */
class WelcomeViewModel(
    studentsRepository: StudentsRepository,
    private val practiseSessionsRepository: PractiseSessionsRepository,
    private val tasksRepository: TasksRepository,
    private val practiseSessionTasksRepository: PractiseSessionTasksRepository) : ViewModel() {
    val welcomeUiState: StateFlow<WelcomeUiState> =
        studentsRepository.getStudentStream(1).map { WelcomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = WelcomeUiState()
            )

    var newSessionId: Long = 0
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
    fun getNewSessionIdAsInt(): Int {
        return newSessionId.toInt()
    }

    suspend fun createNewPractiseSession() {
        newSessionId = practiseSessionsRepository.insertPractiseSession(
            PractiseSession(
                student_id = welcomeUiState.value.student.id,
                start_date = Date(),
                end_date = Date()
            )
        )
        // get current tasks
        tasksRepository.getAllCurrentTasksStream().first().forEach {
            practiseSessionTasksRepository.insertPractiseSessionTask(
                PractiseSessionTask(
                    practise_session_id = newSessionId.toInt(),
                    task_id = it.id,
                    type = it.type,
                    completed = false
                )
            )
        }
        // add all these tasks to the practiseSessionTasks database

    }

    suspend fun createTasks() {
        tasksRepository.insertTask(
            Task(
                title = "C Major Scale",
                student_id = 1,
                type = TaskTypes.EXERCISE.string,
                instructions = "Play a c major scale with the right hand.",
                current = true,
                cues = "Keep your wrist low."
            )
        )
        tasksRepository.insertTask(
            Task(
                title = "C Major Scale",
                student_id = 1,
                type = TaskTypes.EXERCISE.string,
                instructions = "Play a c major scale with the left hand.",
                current = true,
                cues = "Remember the fingering!"
            )
        )
        tasksRepository.insertTask(
            Task(
                title = "Arm swings",
                student_id = 1,
                type = TaskTypes.WARMUP.string,
                instructions = "Play white and black clusters with 2-3-4",
                current = true,
                cues = "Make nice big movements from the shoulder."
            )
        )
        tasksRepository.insertTask(
            Task(
                title = "Shoulder raises",
                student_id = 1,
                type = TaskTypes.WARMUP.string,
                instructions = "Raise your shoulders up to your ears.",
                current = true,
                cues = "Repeat 10 times."
            )
        )
        tasksRepository.insertTask(
            Task(
                title = "Prelude for small cat.",
                student_id = 1,
                type = TaskTypes.PIECE.string,
                instructions = "A cute little piece with a big heart.",
                current = true,
                cues = "Keep your wrist low."
            )
        )
        tasksRepository.insertTask(
            Task(
                title = "Prelude in F Major",
                student_id = 1,
                type = TaskTypes.EXERCISE.string,
                instructions = "An unlikely key for a charming song.",
                current = true,
                cues = "Keep focussed on the rhythm."
            )
        )
    }
}



/**
 * Ui State for WelcomeScreen
 */
data class WelcomeUiState(val student: Student = Student(0, "test", "test", "test", "test", "test", 1, Date(), "test", "12:00"  ))
