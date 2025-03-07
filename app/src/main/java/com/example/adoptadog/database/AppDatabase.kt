package com.example.adoptadog.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Post::class], version = 3)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addMigrations(MIGRATION_1_3) // השתמשנו רק ב-MIGRATION_1_3
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class AppDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        populateDatabase(database.postDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(postDao: PostDao) {
            // כאן תוכל להוסיף נתונים התחלתיים לבסיס הנתונים אם תרצה
        }

        val MIGRATION_1_3 = object : androidx.room.migration.Migration(1, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 1. יצירת טבלה חדשה עם המבנה הרצוי
                database.execSQL("""
                    CREATE TABLE posts_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        imageUrl TEXT NOT NULL,
                        userid TEXT NOT NULL,
                        description TEXT NOT NULL,
                        timestamp INTEGER NOT NULL DEFAULT 0
                    )
                """)

                // 2. העברת נתונים מהטבלה הישנה לחדשה
                database.execSQL("""
                    INSERT INTO posts_new (id, imageUrl, userid, description)
                    SELECT id, '', authorid, content FROM posts
                """)

                // 3. מחיקת הטבלה הישנה
                database.execSQL("DROP TABLE posts")

                // 4. שינוי שם הטבלה החדשה ל-posts
                database.execSQL("ALTER TABLE posts_new RENAME TO posts")
            }
        }
    }
}