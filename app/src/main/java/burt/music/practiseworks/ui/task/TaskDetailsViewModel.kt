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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import burt.music.practiseworks.data.PractiseSessionTasksRepository
import burt.music.practiseworks.data.TasksRepository
import burt.music.practiseworks.ui.item.ItemDetailsDestination

/**
 * ViewModel to retrieve, update and delete a task from the data source.
 */
class TaskDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val tasksRepository: TasksRepository
) : ViewModel() {

    private val itemId: Int = checkNotNull(savedStateHandle[ItemDetailsDestination.itemIdArg])


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}
