package pt.mferreira.classicauctions.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pt.mferreira.classicauctions.data.entities.ItemCategory

@Dao
interface ItemCategoriesDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert(category: ItemCategory)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertAll(categories: List<ItemCategory>)

	@Query("SELECT * FROM item_categories")
	fun getAll(): List<ItemCategory>

	@Query("SELECT * FROM item_categories WHERE id=:id")
	fun getById(id: Int): ItemCategory
}