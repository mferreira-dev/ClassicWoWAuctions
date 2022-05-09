package pt.mferreira.classicauctions.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pt.mferreira.classicauctions.data.entities.ItemSubCategory

@Dao
interface ItemSubCategoriesDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert(category: ItemSubCategory)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertAll(categories: List<ItemSubCategory>)

	@Query("SELECT * FROM item_sub_categories")
	fun getAll(): List<ItemSubCategory>

	@Query("SELECT * FROM item_sub_categories WHERE id=:id")
	fun getById(id: Int): ItemSubCategory
}