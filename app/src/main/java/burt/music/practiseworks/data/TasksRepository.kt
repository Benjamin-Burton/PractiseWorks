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
interface TasksRepository {
    /**
     * Retrieve all the Tasks from the the given data source.
     */
    fun getAllTasksStream(): Flow<List<Task>>

    /**
     * Retrieve an Task from the given data source that matches with the [id].
     */
    fun getTaskStream(id: Int): Flow<Task?>

    fun getTaskPlusCompletedStream(id: Int, sessionId: Int): Flow<TaskPlusCompleted?>
    /**
     * Insert Task in the data source
     */
    suspend fun insertTask(task: Task)

    /**
     * Delete Task from the data source
     */
    suspend fun deleteTask(task: Task)

    /**
     * Update Task in the data source
     */
    suspend fun updateTask(task: Task)

    /**
     * Get all tasks by type
     */
    fun getAllTasksByTypeStream(type: String): Flow<List<Task>>

    fun getCurrentTaskTypes(): Flow<List<TaskNumInfo>>

    fun getNumTasksByType(type: String): Int

    fun getAllCurrentTasksStream(): Flow<List<Task>>
    // note this requires a join
    fun getTasksByTypeAndSessionId(sessionId: Int, type: String): Flow<List<Task>>
    fun getTasksPlusCompletedByTypeAndSessionId(sessionId: Int, type: String): Flow<List<TaskPlusCompleted>>
}
