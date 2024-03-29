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

class OfflineTasksRepository(private val taskDao: TaskDao) : TasksRepository {

    override fun getAllCurrentTasksStream(): Flow<List<Task>> = taskDao.getAllCurrentTasks()
    override fun getAllTasksStream(): Flow<List<Task>> = taskDao.getAllTasks()

    override fun getTaskStream(id: Int): Flow<Task?> = taskDao.getTask(id)

    override fun getTaskPlusCompletedStream(id: Int, sessionId: Int): Flow<TaskPlusCompleted?> = taskDao.getTaskPlusCompleted(id, sessionId)
    override fun getAllTasksByTypeStream(type: String): Flow<List<Task>> =
        taskDao.getTasksByType(type)

    override fun getCurrentTaskTypes(): Flow<List<TaskNumInfo>> =
        taskDao.getCurrentTaskTypes()

    override fun getNumTasksByType(type: String) =
        taskDao.getNumTasksByType(type)

    override fun getTasksByTypeAndSessionId(sessionId: Int, type: String): Flow<List<Task>> =
        taskDao.getTasksByTypeAndSessionId(sessionId, type)

    override fun getTasksPlusCompletedByTypeAndSessionId(sessionId: Int, type: String): Flow<List<TaskPlusCompleted>> =
        taskDao.getTasksPlusCompletedByTypeAndSessionId(sessionId, type)

    override suspend fun insertTask(task: Task) = taskDao.insert(task)

    override suspend fun deleteTask(task: Task) = taskDao.delete(task)

    override suspend fun updateTask(task: Task) = taskDao.update(task)
}
