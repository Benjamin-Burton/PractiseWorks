package burt.music.practiseworks.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(student: Student)
    @Update
    suspend fun update(student: Student)
    @Delete // note you might need to fetch an entity before deleting it
    suspend fun delete(student: Student)
    @Query("SELECT * from student WHERE id = :id")
    fun getStudent(id: Int): Flow<Student> // making this a flow means you only ever have to get the data once - it will be tracked
    // this also means it will auto be on a background thread - so need to make it suspend and call it
    // from a  coroutine scope
    @Query("SELECT * from student ORDER BY f_name ASC")
    fun getAllStudents(): Flow<List<Student>> // this should be ONE
    @Query("SELECT * from student WHERE f_name = :firstName")
    fun getStudentByName(firstName: String): Flow<Student>
}