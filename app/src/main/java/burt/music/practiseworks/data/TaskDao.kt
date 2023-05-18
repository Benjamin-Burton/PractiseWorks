package burt.music.practiseworks.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task)
    @Update
    suspend fun update(task: Task)
    @Delete // note you might need to fetch an entity before deleting it
    suspend fun delete(task: Task)
    @Query("SELECT * from task WHERE id = :id")
    fun getTask(id: Int): Flow<Task> // making this a flow means you only ever have to get the data once - it will be tracked
    // this also means it will auto be on a background thread - so need to make it suspend and call it
    // from a  coroutine scope
    @Query("SELECT t.*, s.completed from task as t, practise_session_task as s WHERE t.id = :id and t.id = s.task_id and s.practise_session_id = :sessionId")
    fun getTaskPlusCompleted(id: Int, sessionId: Int): Flow<TaskPlusCompleted>
    @Query("SELECT * from task ORDER BY id ASC")
    fun getAllTasks(): Flow<List<Task>>
    @Query("SELECT * from task WHERE current = 1")
    fun getAllCurrentTasks(): Flow<List<Task>>
    @Query("SELECT * from task WHERE current = 0")
    fun getALlNonCurrentTasks(): Flow<List<Task>>
    @Query("SELECT * from task WHERE type = :type")
    fun getTasksByType(type: String): Flow<List<Task>>
    @Query("SELECT * from task WHERE type = :type AND current = 1")
    fun getCurrentTasksByType(type: String): Flow<List<Task>>
    @Query("SELECT type, COUNT(type) as count FROM task GROUP BY type")
    fun getCurrentTaskTypes(): Flow<List<TaskNumInfo>>
    @Query("SELECT COUNT(*) FROM task WHERE type = :type")
    fun getNumTasksByType(type: String): Int
    @Query(
        "SELECT id, title, student_id, task.type, instructions, current, cues, track_filename FROM task, practise_session_task WHERE id = task_id AND task.type = :type AND practise_session_id = :sessionId"
    )
    fun getTasksByTypeAndSessionId(sessionId: Int, type: String): Flow<List<Task>>
    @Query(
        "SELECT id, title, student_id, task.type, instructions, current, cues, completed, track_filename FROM task, practise_session_task WHERE id = task_id AND task.type = :type AND practise_session_id = :sessionId"
    )
    fun getTasksPlusCompletedByTypeAndSessionId(sessionId: Int, type: String): Flow<List<TaskPlusCompleted>>
}