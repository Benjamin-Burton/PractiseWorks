package burt.music.practiseworks.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * This provides the instance of the database.
 * Uses a pattern provided by Google codelabs to ensure that
 * the database connection is a singleton.
 *
 * NOTE: Whenever the schema changes, the version number must be incremented.
 * NOTE: Changing the schema deletes all data from the DB unless a Migrator is specified
 * Might want to look into how to do that at some point.
 */
@Database(
    entities = [Item::class, CompletedTask::class, PieceSubtask::class, PractiseSession::class,
                Resource::class, Student::class, Task::class, PractiseSessionTask::class],
    version = 8,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PractiseWorksDatabase: RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun completedTaskDao(): CompletedTaskDao
    abstract fun pieceSubtaskDao(): PieceSubtaskDao
    abstract fun practiseSessionDao(): PractiseSessionDao
    abstract fun resourceDao(): ResourceDao
    abstract fun studentDao(): StudentDao
    abstract fun taskDao(): TaskDao
    abstract fun practiseSessionTaskDao(): PractiseSessionTaskDao

    // to ensure singleton
    companion object {
        @Volatile
        private var Instance: PractiseWorksDatabase? = null

        fun getDatabase(context: Context): PractiseWorksDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context,
                    burt.music.practiseworks.data.PractiseWorksDatabase::class.java,
                    "practiseworks_db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}



