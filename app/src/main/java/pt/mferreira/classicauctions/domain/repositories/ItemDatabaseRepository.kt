package pt.mferreira.classicauctions.domain.repositories

import pt.mferreira.classicauctions.data.entities.ItemCategory
import pt.mferreira.classicauctions.data.entities.ItemSubCategory

interface ItemDatabaseRepository {
	suspend fun insertItemCategory(itemCategory: ItemCategory)
	suspend fun insertAllItemCategories(itemCategories: List<ItemCategory>)
	suspend fun getAllItemCategories(): List<ItemCategory>
	suspend fun getItemCategoryById(id: Int): ItemCategory

	suspend fun insertItemSubCategory(itemSubCategory: ItemSubCategory)
	suspend fun insertAllItemSubCategories(itemSubCategories: List<ItemSubCategory>)
	suspend fun getAllItemSubCategories(): List<ItemSubCategory>
	suspend fun getItemSubCategoryById(id: Int): ItemSubCategory
}