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
 * Represents a resource that belongs to a task for the student.
 */
@Entity(tableName = "practise_session", foreignKeys = [ForeignKey(entity = Student::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("student_id"),
    onDelete = ForeignKey.CASCADE)]
)
data class PractiseSession(
    @PrimaryKey (autoGenerate = true)
    val id: Int = 0,
    val student_id: Int,
    val start_date: Date?,
    val end_date: Date?,
)
