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

package burt.music.practiseworks.ui.student

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import burt.music.practiseworks.data.StudentsRepository
import burt.music.practiseworks.data.TasksRepository
import burt.music.practiseworks.ui.student.isValid
import burt.music.practiseworks.ui.task.TaskUiState
import burt.music.practiseworks.ui.task.isValid
import java.util.*

/**
 * View Model to validate and insert students in the Room database.
 */
class StudentEntryViewModel(private val studentsRepository: StudentsRepository) : ViewModel() {

    /**
     * Holds current student ui state
     */
    var studentUiState by mutableStateOf(StudentUiState())
        private set

    /**
     * Updates the [studentUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(newStudentUiState: StudentUiState) {
        studentUiState = newStudentUiState.copy( actionEnabled = newStudentUiState.isValid())
    }

    suspend fun saveStudent() {
        if (studentUiState.isValid()) {
            studentsRepository.insertStudent(studentUiState.toStudent())
        }
    }
}
