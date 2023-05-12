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

package burt.music.practiseworks.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import burt.music.practiseworks.ui.home.HomeDestination
import burt.music.practiseworks.ui.home.HomeScreen
import burt.music.practiseworks.ui.item.*
import burt.music.practiseworks.ui.listScreens.PractiseScreen
import burt.music.practiseworks.ui.listScreens.PractiseScreenDestination
import burt.music.practiseworks.ui.listScreens.TaskListDestination
import burt.music.practiseworks.ui.listScreens.TaskListScreen
import burt.music.practiseworks.ui.student.StudentEntryDestination
import burt.music.practiseworks.ui.student.StudentEntryScreen
import burt.music.practiseworks.ui.task.TaskEntryDestination
import burt.music.practiseworks.ui.task.TaskEntryScreen
import burt.music.practiseworks.ui.welcome.WelcomeDestination
import burt.music.practiseworks.ui.welcome.WelcomeScreen

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun PractiseWorksNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = WelcomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToFirstEntry = { navController.navigate(ItemEntryDestination.route) },
                navigateToItemUpdate = {
                    navController.navigate("${ItemDetailsDestination.route}/${it}")
                }
            )
        }
        composable(route = ItemEntryDestination.route) {
            ItemEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = ItemDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemDetailsDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ItemDetailsScreen(
                navigateToEditItem = { navController.navigate("${ItemEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = ItemEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemEditDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ItemEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = TaskEntryDestination.route
        ) {
            TaskEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() })
        }
        composable(
            route = TaskListDestination.route
        ) {
            TaskListScreen(
                navigateToTaskUpdate = {
                    navController.navigate("${ItemDetailsDestination.route}/${it}")}
            )
        }
        composable(
            route = StudentEntryDestination.route
        ) {
            StudentEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() })
        }
        composable(
            route = PractiseScreenDestination.routeWithArgs,
            arguments = listOf(navArgument(PractiseScreenDestination.practiseIdArg) {
                type = NavType.IntType
            })
        ) {
            PractiseScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
            )
        }
        composable(
            route = WelcomeDestination.route
        ) {
            WelcomeScreen(
                navigateToFirstEntry = {},
                navigateToItemUpdate = {},
                navigateToNewPractiseSession = {
                    navController.navigate("${PractiseScreenDestination.route}/${it}")
                }
            )
        }
    }
}
