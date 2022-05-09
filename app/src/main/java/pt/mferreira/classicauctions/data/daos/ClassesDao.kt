package pt.mferreira.classicauctions.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pt.mferreira.classicauctions.data.entities.Class

@Dao
interface ClassesDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert(aClass: Class)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertAll(classes: List<Class>)

	@Query("SELECT * FROM classes")
	fun getAll(): List<Class>

	@Query("SELECT * FROM classes WHERE id=:id")
	fun getById(id: String): Class
}