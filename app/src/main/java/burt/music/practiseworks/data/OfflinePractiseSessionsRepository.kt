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

class OfflinePractiseSessionsRepository(private val practiseSessionDao: PractiseSessionDao) : PractiseSessionsRepository {
    override fun getAllPractiseSessionsStream(): Flow<List<PractiseSession>> = practiseSessionDao.getAllPractiseSessions()

    override fun getPractiseSessionStream(id: Int): Flow<PractiseSession> = practiseSessionDao.getPractiseSession(id)

    override suspend fun insertPractiseSession(practiseSession: PractiseSession) = practiseSessionDao.insert(practiseSession)

    override suspend fun deletePractiseSession(practiseSession: PractiseSession) = practiseSessionDao.delete(practiseSession)

    override suspend fun updatePractiseSession(practiseSession: PractiseSession) = practiseSessionDao.update(practiseSession)
}
