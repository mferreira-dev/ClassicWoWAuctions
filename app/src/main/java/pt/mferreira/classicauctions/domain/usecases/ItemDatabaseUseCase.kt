package pt.mferreira.classicauctions.domain.usecases

import pt.mferreira.classicauctions.data.entities.ItemCategory
import pt.mferreira.classicauctions.data.entities.ItemSubCategory
import pt.mferreira.classicauctions.domain.repositories.ItemDatabaseRepository

class ItemDatabaseUseCase(private val repository: ItemDatabaseRepository) {
	suspend fun insertItemCategory(itemCategory: ItemCategory) =
		repository.insertItemCategory(itemCategory)

	suspend fun insertAllItemCategories(itemCategories: List<ItemCategory>) =
		repository.insertAllItemCategories(itemCategories)

	suspend fun getAllItemCategories(): List<ItemCategory> = repository.getAllItemCategories()
	suspend fun getItemCategoryById(id: Int): ItemCategory = repository.getItemCategoryById(id)

	suspend fun insertItemSubCategory(itemSubCategory: ItemSubCategory) =
		repository.insertItemSubCategory(itemSubCategory)

	suspend fun insertAllItemSubCategories(itemSubCategories: List<ItemSubCategory>) =
		repository.insertAllItemSubCategories(itemSubCategories)

	suspend fun getAllItemSubCategories(): List<ItemSubCategory> =
		repository.getAllItemSubCategories()

	suspend fun getItemSubCategoryById(id: Int): ItemSubCategory =
		repository.getItemSubCategoryById(id)
}