package burt.music.practiseworks.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ResourceDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(resource: Resource)
    @Update
    suspend fun update(resource: Resource)
    @Delete // note you might need to fetch an entity before deleting it
    suspend fun delete(resource: Resource)
    @Query("SELECT * from resource WHERE id = :id")
    fun getResource(id: Int): Flow<Resource> // making this a flow means you only ever have to get the data once - it will be tracked
    // this also means it will auto be on a background thread - so need to make it suspend and call it
    // from a  coroutine scope
    @Query("SELECT * from resource WHERE task_id = :taskId")
    fun getResourcesByTask(taskId: Int): Flow<List<Resource>>
    @Query("SELECT * from resource ORDER BY id ASC")
    fun getAllResources(): Flow<List<Resource>>

}