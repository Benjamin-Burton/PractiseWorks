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
import java.util.*

/**
 * Represents a task that belongs to a particular practise session.
 */
@Entity(tableName = "practise_session_task",
    primaryKeys = ["practise_session_id", "task_id"],
    foreignKeys = [
        ForeignKey(entity = PractiseSession::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("practise_session_id"),
        onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Task::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("task_id"),
        onDelete = ForeignKey.CASCADE)
    ],
)
data class PractiseSessionTask(
    val practise_session_id: Int,
    val task_id: Int,
    val type: String, // i'm including this so we don't have to join so often
    val completed: Boolean,
)

data class PractiseSessionCountInfo(
    val type: String,
    val numTotal: Int,
    val numCompleted: Int
)

data class PractiseSessionTypeNumCompleted(
    val type: String,
    val numCompleted: Int
)
