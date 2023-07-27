package burt.music.practiseworks.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val itemsRepository: ItemsRepository
    val studentsRepository: StudentsRepository
    val tasksRepository: TasksRepository
    val practiseSessionsRepository: PractiseSessionsRepository
    val practiseSessionTasksRepository: PractiseSessionTasksRepository
}

/**
 * [AppContainer] implementation that provides instance of repositories.
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [ItemsRepository]
     */
    override val itemsRepository: ItemsRepository by lazy {
        OfflineItemsRepository(PractiseWorksDatabase.getDatabase(context).itemDao())
    }

    override val studentsRepository: StudentsRepository by lazy {
        OfflineStudentsRepository(PractiseWorksDatabase.getDatabase(context).studentDao())
    }

    override val tasksRepository: TasksRepository by lazy {
        OfflineTasksRepository(PractiseWorksDatabase.getDatabase(context).taskDao())
    }

    override val practiseSessionsRepository: PractiseSessionsRepository by lazy {
        OfflinePractiseSessionsRepository(PractiseWorksDatabase.getDatabase(context).practiseSessionDao())
    }

    override val practiseSessionTasksRepository: PractiseSessionTasksRepository by lazy {
        OfflinePractiseSessionTasksRepository(PractiseWorksDatabase.getDatabase(context).practiseSessionTaskDao())
    }

}