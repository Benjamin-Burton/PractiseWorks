package burt.music.practiseworks.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface PractiseSessionTaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(practiseSessionTask: PractiseSessionTask): Long
    @Update
    suspend fun update(practiseSessionTask: PractiseSessionTask)
    @Delete // note you might need to fetch an entity before deleting it
    suspend fun delete(practiseSessionTask: PractiseSessionTask)
    @Query("SELECT * from practise_session_task WHERE practise_session_id = :practiseSessionId")
    fun getPractiseSessionTasksBySessionId(practiseSessionId: Int): Flow<List<PractiseSessionTask>> // making this a flow means you only ever have to get the data once - it will be tracked
    // this also means it will auto be on a background thread - so need to make it suspend and call it
    // from a  coroutine scope
    @Query("SELECT * from practise_session_task WHERE type = :type AND practise_session_id = :practiseSessionId")
    fun getPractiseSessionTasksByTypeAndSessionId(type: String, practiseSessionId: Int): Flow<List<PractiseSessionTask>>
    @Query("SELECT type, COUNT(type) as numTotal, COUNT(completed) as numCompleted FROM practise_session_task WHERE practise_session_id = :practiseSessionId GROUP BY type")
    fun getNumTasksCompletedInfo(practiseSessionId: Int): Flow<List<PractiseSessionCountInfo>>
    // TODO work out how to include the bool in this query - so that we have
    // TODO type, count of type, num completed of type
    // Solution - have two separate quereies
    @Query("SELECT type, COUNT(*) as numCompleted FROM practise_session_task WHERE practise_session_id = :practiseSessionId AND completed = 1 GROUP BY type")
    fun getNumCompletedTasksBySessionIdAndType(practiseSessionId: Int): Flow<List<PractiseSessionTypeNumCompleted>>
}