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

package burt.music.practiseworks.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Item] from a given data source.
 */
interface StudentsRepository {
    /**
     * Retrieve all the Students from the the given data source.
     */
    fun getAllStudentsStream(): Flow<List<Student>>

    /**
     * Retrieve an Student from the given data source that matches with the [id].
     */
    fun getStudentStream(id: Int): Flow<Student>

    /**
     * Insert Student in the data source
     */
    suspend fun insertStudent(student: Student)

    /**
     * Delete Student from the data source
     */
    suspend fun deleteStudent(student: Student)

    /**
     * Update Student in the data source
     */
    suspend fun updateStudent(student: Student)
}
