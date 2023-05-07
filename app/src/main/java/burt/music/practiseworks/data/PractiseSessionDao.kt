package burt.music.practiseworks.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface PractiseSessionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(practiseSession: PractiseSession)
    @Update
    suspend fun update(practiseSession: PractiseSession)
    @Delete // note you might need to fetch an entity before deleting it
    suspend fun delete(practiseSession: PractiseSession)
    @Query("SELECT * from practise_session WHERE id = :id")
    fun getPractiseSession(id: Int): Flow<PractiseSession> // making this a flow means you only ever have to get the data once - it will be tracked
    // this also means it will auto be on a background thread - so need to make it suspend and call it
    // from a  coroutine scope
    @Query("SELECT * from practise_session")
    fun getAllPractiseSessions(): Flow<List<PractiseSession>>
    @Query("SELECT * from practise_session WHERE start_date = :date")
    // TODO - implement this properly
    fun getPractiseSessionsByDate(date: Date): Flow<List<PractiseSession>>

}