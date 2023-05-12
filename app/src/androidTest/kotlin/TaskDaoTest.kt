import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import burt.music.practiseworks.data.*
import burt.music.practiseworks.ui.task.TaskTypes
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

@RunWith(AndroidJUnit4::class)
class TaskDaoTest {
    private lateinit var taskDao: TaskDao
    private lateinit var studentDao: StudentDao
    private lateinit var practiseWorksDatabase: PractiseWorksDatabase

    private var task1 = Task(1, "C major", 1, TaskTypes.WARMUP.string, "Play it right", true, "Again: lower your wrist.")
    private var task2 = Task(2, "Mah ah ah", 1, TaskTypes.WARMUP.string, "And close it", true, "And open it")
    private var task3 = Task(3, "Giant Ball", 1, TaskTypes.WARMUP.string, "C major scale.", true, "1,2,3,1,2,3,4,5")

    private var student1 = Student(
        id = 1,
        f_name = "Ben",
        l_name = "Burton",
        username = "berny",
        email = "berny222@hotmail.com",
        password = "notreal",
        teacher_id = 1,
        join_date = Date(),
        lesson_day = "Monday",
        lesson_time = "7:30pm"
    )
    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        practiseWorksDatabase = Room.inMemoryDatabaseBuilder(context, PractiseWorksDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        taskDao = practiseWorksDatabase.taskDao()
        studentDao = practiseWorksDatabase.studentDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        practiseWorksDatabase.close()
    }

    // utility functions
    // mark them as suspend so they can be run in a coroutine scope (necessary for the DAO)
    private suspend fun addOneTaskToDb() {
        taskDao.insert(task1)
    }

    private suspend fun addTwoTasksToDb() {
        taskDao.insert(task1)
        taskDao.insert(task2)
    }

    private suspend fun addStudentToDb() {
        studentDao.insert(student1)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsTaskIntoDB() = runBlocking {
        addStudentToDb()
        addOneTaskToDb()
        val allTasks = taskDao.getAllTasks().first()
        assertEquals(allTasks[0], task1)
    }
    @Test
    @Throws(Exception::class)
    fun daoGetAllTasks_returnsAllTasksFromDB() = runBlocking {
        addStudentToDb()
        addTwoTasksToDb()
        val allTasks = taskDao.getAllTasks().first()
        assertEquals(allTasks[0], task1)
        assertEquals(allTasks[1], task2)
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateItems_updatesTasksInDB() = runBlocking {
        addStudentToDb()
        addTwoTasksToDb()
        taskDao.update(Task(1, "East Side", 1, TaskTypes.WARMUP.string, "Play it right", true, "Again: lower your elbow."))
        taskDao.update(Task(2, "Michael West", 1, TaskTypes.WARMUP.string, "And close it", true, "And tighten it"))

        val allTasks = taskDao.getAllTasks().first()
        assertEquals(allTasks[0], Task(1, "East Side", 1, TaskTypes.WARMUP.string, "Play it right", true, "Again: lower your elbow."))
        assertEquals(allTasks[1], Task(2, "Michael West", 1, TaskTypes.WARMUP.string, "And close it", true, "And tighten it"))
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteTasks_DeletesTasksInDB() = runBlocking {
        addStudentToDb()
        addTwoTasksToDb()
        taskDao.delete(task1)
        taskDao.delete(task2)

        val allTasks = taskDao.getAllTasks().first()
        assertTrue(allTasks.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoGetTasks_GetsTasksInDB() = runBlocking {
        addStudentToDb()
        addTwoTasksToDb()
        val get_task1 = taskDao.getTask(task1.id)
        val get_task2 = taskDao.getTask(task2.id)


        // remember .first() stops the collecting flow immediately - won't work otherwise
        assertEquals(task1, get_task1.first())
        assertEquals(task2, get_task2.first())
    }
}

