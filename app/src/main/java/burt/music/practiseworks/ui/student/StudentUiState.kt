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
import burt.music.practiseworks.data.Student
import burt.music.practiseworks.ui.item.ItemUiState
import java.util.*

/**
 * Represents Ui State for a Student.
 */
data class StudentUiState(
    val id: Int = 0,
    val f_name: String = "",
    val l_name: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val teacher_id: Int = 0,
    val join_date: Date = Date(),
    val lesson_day: String = "",
    val lesson_time: String = "",
    val actionEnabled: Boolean = false,
    val practiseGoal: Int = 0,
    val totalPoints: Int = 0
)

/**
 * Extension function to convert [StudentUiState] to [Student].
 */
fun StudentUiState.toStudent(): Student = Student(
    id = id,
    f_name = f_name,
    l_name = l_name,
    username = username,
    email = email,
    password = password,
    teacher_id = teacher_id,
    join_date = join_date,
    lesson_day = lesson_day,
    lesson_time = lesson_time,
    practise_goal = practiseGoal,
    total_points = totalPoints
)

/**
 * Extension function to convert [Student] to [StudentUiState]
 */
fun Student.toStudentUiState(actionEnabled: Boolean = false): StudentUiState = StudentUiState(
    id = id,
    f_name = f_name,
    l_name = l_name,
    username = username,
    email = email,
    password = password,
    teacher_id = teacher_id,
    join_date = join_date,
    lesson_day = lesson_day,
    lesson_time = lesson_time,
    actionEnabled = actionEnabled,
    practiseGoal = practise_goal,
    totalPoints = total_points
)

fun StudentUiState.isValid() : Boolean {
    return f_name.isNotBlank() &&
            l_name.isNotBlank() &&
            username.isNotBlank() &&
            email.isNotBlank() &&
            password.isNotBlank() &&
            teacher_id != 0
 }
