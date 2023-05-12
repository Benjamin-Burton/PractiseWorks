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

package burt.music.practiseworks.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import burt.music.practiseworks.PractiseWorksApplication
import burt.music.practiseworks.ui.listScreens.TaskListViewModel
import burt.music.practiseworks.ui.item.ItemDetailsViewModel
import burt.music.practiseworks.ui.item.ItemEditViewModel
import burt.music.practiseworks.ui.item.ItemEntryViewModel
import burt.music.practiseworks.ui.home.HomeViewModel
import burt.music.practiseworks.ui.listScreens.PractiseScreenViewModel
import burt.music.practiseworks.ui.student.StudentEntryViewModel
import burt.music.practiseworks.ui.task.TaskDetailsViewModel
import burt.music.practiseworks.ui.task.TaskEntryViewModel
import burt.music.practiseworks.ui.welcome.WelcomeViewModel


/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {

        // Initializer for ItemEntryViewModel
        initializer {
            ItemEntryViewModel(practiseWorksApplication().container.itemsRepository)
        }

        // Initializer for ItemDetailsViewModel
        initializer {
            ItemDetailsViewModel(
                this.createSavedStateHandle(),
                practiseWorksApplication().container.itemsRepository
            )
        }

        // Initializer for ItemEditViewModel
        initializer {
            ItemEditViewModel(
                this.createSavedStateHandle(),
                practiseWorksApplication().container.itemsRepository
            )
        }

        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(practiseWorksApplication().container.itemsRepository)
        }

        // Initializer for TaskDetailsViewModel
        initializer {
            TaskDetailsViewModel(this.createSavedStateHandle())
        }

        // Initializer for TaskEntryViewModel
        initializer {
            TaskEntryViewModel(practiseWorksApplication().container.tasksRepository)
        }

        // Initializer for TaskListViewModel
        initializer {
            TaskListViewModel(practiseWorksApplication().container.tasksRepository)
        }

        // Initializer for StudentEntryViewModel
        initializer {
            StudentEntryViewModel(practiseWorksApplication().container.studentsRepository)
        }

        // Initializer for WelcomeViewModel
        initializer {
            WelcomeViewModel(practiseWorksApplication().container.studentsRepository,
                            practiseWorksApplication().container.practiseSessionsRepository,
                            practiseWorksApplication().container.tasksRepository,
                            practiseWorksApplication().container.practiseSessionTasksRepository
            )
        }

        // Initializer for PractiseScreenViewModel
        initializer {
            PractiseScreenViewModel(
                this.createSavedStateHandle(),
                practiseWorksApplication().container.tasksRepository,
                practiseWorksApplication().container.practiseSessionsRepository,
                practiseWorksApplication().container.practiseSessionTasksRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [PractiseWorksApplication].
 */
fun CreationExtras.practiseWorksApplication(): PractiseWorksApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as PractiseWorksApplication)
