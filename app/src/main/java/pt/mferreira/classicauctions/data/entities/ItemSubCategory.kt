package pt.mferreira.classicauctions.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_sub_categories")
data class ItemSubCategory(
	@PrimaryKey(autoGenerate = true) val key: Int,
	@ColumnInfo(name = "categoryId") val categoryId: Int,
	@ColumnInfo(name = "id") val id: Int,
	@ColumnInfo(name = "name") val name: String,
)