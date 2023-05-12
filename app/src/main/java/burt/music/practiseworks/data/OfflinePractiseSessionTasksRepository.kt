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

class OfflinePractiseSessionTasksRepository(private val practiseSessionTaskDao: PractiseSessionTaskDao) : PractiseSessionTasksRepository {
    override fun getPractiseSessionTasksByTypeAndSessionIdStream(type: String, id: Int): Flow<List<PractiseSessionTask>> = practiseSessionTaskDao.getPractiseSessionTasksByTypeAndSessionId(type, id)

    override fun getPractiseSessionTasksBySessionIdStream(practiseSessionId: Int): Flow<List<PractiseSessionTask>> = practiseSessionTaskDao.getPractiseSessionTasksBySessionId(practiseSessionId)

    override fun getNumTasksCompletedInfo(practiseSessionId: Int): Flow<List<PractiseSessionCountInfo>> = practiseSessionTaskDao.getNumTasksCompletedInfo(practiseSessionId)

    override fun getNumCompletedTasksBySessionIdAndType(practiseSessionId: Int): Flow<List<PractiseSessionTypeNumCompleted>> = practiseSessionTaskDao.getNumCompletedTasksBySessionIdAndType(practiseSessionId)

    override suspend fun insertPractiseSessionTask(sessionTask: PractiseSessionTask) = practiseSessionTaskDao.insert(sessionTask)

    override suspend fun deletePractiseSessionTask(sessionTask: PractiseSessionTask) = practiseSessionTaskDao.delete(sessionTask)

    override suspend fun updatePractiseSessionTask(sessionTask: PractiseSessionTask) = practiseSessionTaskDao.update(sessionTask)
}
