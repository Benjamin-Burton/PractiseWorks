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
                title = "Exercise 1 - ng",
                student_id = 1,
                type = TaskTypes.WARMUP.string,
                instructions = "Glissando over an octave on the sound 'ng'",
                current = true,
                cues = "Release the jaw.\nSlide very slowly between notes.",
                track_filename = "exercise_1_ng.mp3"
            )
        )
        tasksRepository.insertTask(
            Task(
                title = "Exercise 2 - 5-note scale",
                student_id = 1,
                type = TaskTypes.WARMUP.string,
                instructions = "Sing the five Italian vowels on a 5-note scale.\n",
                current = true,
                cues = "Place the tongue forward.\nRemember the tip of the tongue should be just behind the bottom teeth.",
                track_filename = "exercise_2_5_note_scale.mp3"
            )
        )
        tasksRepository.insertTask(
            Task(
                title = "Exercise 3 - Major Triad",
                student_id = 1,
                type = TaskTypes.WARMUP.string,
                instructions = "Singing on the Italian Vowels, moving higher this time.",
                current = true,
                cues = "As the sound moves higher, use extra support.\nRemember to think 'up and over' as the exercise moves higher.",
                track_filename = "exercise_3_major_triad.mp3"
            )
        )
        tasksRepository.insertTask(
            Task(
                title = "Exercise 4 - Scale in Thirds",
                student_id = 1,
                type = TaskTypes.WARMUP.string,
                instructions = "Now it's time to check the posture.\nCheck for tension.\nMake sure you are relaxed.",
                current = true,
                cues = "We are still in the mid voice - not maximum energy yet.\nKeep the tongue forward and behind the front teeth",
                track_filename = "exercise_4_scale_in_3rds.mp3"
            )
        )
        tasksRepository.insertTask(
            Task(
                title = "Exercise 5 - 9-note scale",
                student_id = 1,
                type = TaskTypes.WARMUP.string,
                instructions = "We now move higher. As you get to the top of this scale, you'll need to engage your support.\nWe still use the Italian vowels.",
                current = true,
                cues = "As you get higher, you'll need to modify the vowel.\nDrop the jaw.\nKeep the tongue in a relaxed position.\nKeep the tongue forward.",
                track_filename = "exercise_5_9_note_scale.mp3"
            )
        )
        tasksRepository.insertTask(
            Task(
                title = "Exercise 6 - Major Arpeggio",
                student_id = 1,
                type = TaskTypes.WARMUP.string,
                instructions = "We are now moving into the top range of the voice.\nStill using all Italian vowels, starting with 'mi'",
                current = true,
                cues = "Lots of support is required.\nGood placement.\nRelease when you take the breath.",
                track_filename = "exercise_6_major_arpeggio.mp3"
            )
        )
        tasksRepository.insertTask(
            Task(
                title = "Exercise 7 - Scales in Thirds",
                student_id = 1,
                type = TaskTypes.WARMUP.string,
                instructions = "Scales in thirds over the full major tenth.\nStarting with 'me-ah'",
                current = true,
                cues = "Make sure:\nThinking up and over.\nGood release when you take the breath!",
                track_filename = "exercise_7_scales_in_thirds.mp3"
            )
        )
        tasksRepository.insertTask(
            Task(
                title = "Standchen",
                student_id = 1,
                type = TaskTypes.PIECE.string,
                instructions = "A famous and beautiful serenade by Franz Schubert.",
                current = true,
                cues = "Refer to the resources for German pronunciation guide.",
                track_filename = "standchen_piano.mp3"
            )
        )
        tasksRepository.insertTask(
            Task(
                title = "Take the A-train",
                student_id = 1,
                type = TaskTypes.PIECE.string,
                instructions = "What a banger! Practise hard to sound like ELla :)",
                current = true,
                cues = "If you aren't having fun, neither is your audience.",
                track_filename = "take_the_a_train.mp3"
            )
        )
        tasksRepository.insertTask(
            Task(
                title = "I'm beginning to see the light.",
                student_id = 1,
                type = TaskTypes.PIECE.string,
                instructions = "I'm beginning to see the light.",
                current = true,
                cues = "Just keep singing it through. Focus on memorising the words.",
                track_filename = "beginning_to_see_the_light_backing_1.mp3"
            )
        )
    }
}



/**
 * Ui State for WelcomeScreen
 */
data class WelcomeUiState(val student: Student = Student(0, "test", "test", "test", "test", "test", 1, Date(), "test", "12:00", practise_goal = 5, total_points = 4000))
