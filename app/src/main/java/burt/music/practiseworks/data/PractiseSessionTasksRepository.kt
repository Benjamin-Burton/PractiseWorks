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
interface PractiseSessionTasksRepository {
    /**
     * Retrieve all the PractiseSessions from the the given data source.
     */
    fun getPractiseSessionTasksBySessionIdStream(practiseSessionId: Int): Flow<List<PractiseSessionTask>>

    fun getNumCompletedTasksBySessionIdAndType(practiseSessionId: Int): Flow<List<PractiseSessionTypeNumCompleted>>
    /**
     * Retrieve an PractiseSession from the given data source that matches with the [id].
     */
    fun getPractiseSessionTasksByTypeAndSessionIdStream(type: String, id: Int): Flow<List<PractiseSessionTask>>

    fun getNumTasksCompletedInfo(practiseSessionId: Int): Flow<List<PractiseSessionCountInfo>>
    /**
     * Insert PractiseSession in the data source
     */
    suspend fun insertPractiseSessionTask(sessionTask: PractiseSessionTask): Long

    /**
     * Delete PractiseSession from the data source
     */
    suspend fun deletePractiseSessionTask(sessionTask: PractiseSessionTask)

    /**
     * Update PractiseSession in the data source
     */
    suspend fun updatePractiseSessionTask(sessionTask: PractiseSessionTask)


}
