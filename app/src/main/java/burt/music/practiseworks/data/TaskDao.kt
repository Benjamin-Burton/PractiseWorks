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
    @Query("SELECT * from task ORDER BY id ASC")
    fun getAllTasks(): Flow<List<Task>>
    @Query("SELECT * from task WHERE current = true")
    fun getAllCurrentTasks(): Flow<List<Task>>
    @Query("SELECT * from task WHERE current - false")
    fun getALlNonCurrentTasks(): Flow<List<Task>>
    @Query("SELECT * from task WHERE type = :type")
    fun getTasksByType(type: String): Flow<List<Task>>
    @Query("SELECT * from task WHERE type = :type AND current = true")
    fun getCurrentTasksByType(type: String): Flow<List<Task>>
}