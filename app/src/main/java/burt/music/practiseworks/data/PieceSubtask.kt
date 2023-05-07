/*
 * Copyright (C) 2022 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package burt.music.practiseworks.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Represents a sub task within a piece task.
 */
@Entity(tableName = "piece_subtask", foreignKeys = [ForeignKey(entity = Task::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("task_id"),
    onDelete = ForeignKey.CASCADE)]
)
data class PieceSubtask(
    @PrimaryKey
    val id: Int,
    val task_id: Int, // the piece task that this is a subtask of
    val instructions: String
)
