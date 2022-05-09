package pt.mferreira.classicauctions.data.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pt.mferreira.classicauctions.data.daos.*
import pt.mferreira.classicauctions.data.entities.*

@Database(
	entities = [
		Realm::class,
		ItemCategory::class,
		ItemSubCategory::class,
		Class::class,
		PvPEntry::class
	],
	version = 1
)
abstract class AppDatabase : RoomDatabase() {
	abstract fun realmsDao(): RealmsDao
	abstract fun itemCategoriesDao(): ItemCategoriesDao
	abstract fun itemSubCategoriesDao(): ItemSubCategoriesDao
	abstract fun classesDao(): ClassesDao
	abstract fun pvpDao(): PvPDao

	companion object {
		@Volatile
		private var instance: AppDatabase? = null

		fun getInstance(context: Context) =
			instance ?: synchronized(this) {
				instance ?: Room.databaseBuilder(
					context.applicationContext,
					AppDatabase::class.java,
					"app_database"
				)
					.fallbackToDestructiveMigration()
					.allowMainThreadQueries()
					.build()
			}
	}
}