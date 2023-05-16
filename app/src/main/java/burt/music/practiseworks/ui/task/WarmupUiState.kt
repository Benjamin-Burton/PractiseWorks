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
import burt.music.practiseworks.data.StudentDao
import burt.music.practiseworks.data.StudentsRepository
import burt.music.practiseworks.data.Task
import burt.music.practiseworks.ui.item.ItemUiState

/**
 * Represents Ui State for a Task.
 */
data class WarmupUiState(
    val id: Int = 0,
    val title: String = "",
    val student_id: Int = 1,
    val type: TaskTypes = TaskTypes.OTHER,
    val instructions: String = "",
    val current: Boolean = false,
    val cues: String = "",
    val actionEnabled: Boolean = false,
)

/**
 * Extension function to convert [TaskUiState] to [Task]. If the value of [ItemUiState.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [ItemUiState] is not a valid [Int], then the quantity will be set to 0
 */
fun WarmupUiState.toTask(): Task = Task(
    id = id,
    title = title,
    student_id = student_id,
    type = type.string,
    instructions = instructions,
    current = current,
    cues = cues
)

/**
 * Extension function to convert [Task] to [TaskUiState]
 */
fun Task.toWarmupUiState(): WarmupUiState = WarmupUiState(
    id = id,
    title = title,
    student_id = student_id,
    type = TaskTypes.valueOf(type.uppercase()),
    instructions = instructions,
    current = current,
    cues = cues,
)

fun WarmupUiState.isValid() : Boolean {
    return title.isNotBlank() && instructions.isNotBlank() && cues.isNotBlank() && type.string != ""
 }
