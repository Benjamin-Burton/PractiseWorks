package burt.music.practiseworks.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
    entities = [Item::class, CompletedTask::class, PieceSubtask::class, PractiseSession::class,
                Resource::class, Student::class, Task::class],
    version = 1,
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



