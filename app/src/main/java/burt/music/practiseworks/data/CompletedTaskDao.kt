package burt.music.practiseworks.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CompletedTaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(completedTask: CompletedTask)
    @Update
    suspend fun update(completedTask: CompletedTask)
    @Delete // note you might need to fetch an entity before deleting it
    suspend fun delete(completedTask: CompletedTask)
    @Query("SELECT * from completed_task WHERE task_id = :taskId")
    fun getCompletedTasksByTask(taskId: Int): Flow<List<CompletedTask>> // making this a flow means you only ever have to get the data once - it will be tracked
    // this also means it will auto be on a background thread - so need to make it suspend and call it
    // from a  coroutine scope
    @Query("SELECT * from completed_task ORDER BY task_id ASC")
    fun getAllCompletedTasks(): Flow<List<CompletedTask>>
    @Query("SELECT * from completed_task WHERE practise_session_id = :practiseId")
    fun getCompletedTasksByPractiseSession(practiseId: Int): Flow<List<CompletedTask>>

}