package burt.music.practiseworks.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PieceSubtaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(pieceSubTask: PieceSubtask)
    @Update
    suspend fun update(pieceSubTask: PieceSubtask)
    @Delete // note you might need to fetch an entity before deleting it
    suspend fun delete(pieceSubTask: PieceSubtask)
    @Query("SELECT * from piece_subtask WHERE id = :id")
    fun getPieceSubTask(id: Int): Flow<PieceSubtask> // making this a flow means you only ever have to get the data once - it will be tracked
    // this also means it will auto be on a background thread - so need to make it suspend and call it
    // from a  coroutine scope
    @Query("SELECT * from piece_subtask ORDER BY id ASC")
    fun getAllSubTasks(): Flow<List<PieceSubtask>>
    @Query("SELECT * from piece_subtask WHERE task_id = :taskId")
    fun getSubTasksByTask(taskId: Int): Flow<List<PieceSubtask>>

}