package pt.mferreira.classicauctions.data.repositories

import android.app.Application
import pt.mferreira.classicauctions.data.databases.AppDatabase
import pt.mferreira.classicauctions.data.entities.ItemCategory
import pt.mferreira.classicauctions.data.entities.ItemSubCategory
import pt.mferreira.classicauctions.domain.repositories.ItemDatabaseRepository

class ItemDatabaseRepositoryImpl(application: Application) : ItemDatabaseRepository {
	private val itemCategoriesDao = AppDatabase.getInstance(application).itemCategoriesDao()
	private val itemSubCategoriesDao = AppDatabase.getInstance(application).itemSubCategoriesDao()

	override suspend fun insertItemCategory(itemCategory: ItemCategory) =
		itemCategoriesDao.insert(itemCategory)

	override suspend fun insertAllItemCategories(itemCategories: List<ItemCategory>) =
		itemCategoriesDao.insertAll(itemCategories)

	override suspend fun getAllItemCategories(): List<ItemCategory> = itemCategoriesDao.getAll()
	override suspend fun getItemCategoryById(id: Int): ItemCategory = itemCategoriesDao.getById(id)

	override suspend fun insertItemSubCategory(itemSubCategory: ItemSubCategory) =
		itemSubCategoriesDao.insert(itemSubCategory)

	override suspend fun insertAllItemSubCategories(itemSubCategories: List<ItemSubCategory>) =
		itemSubCategoriesDao.insertAll(itemSubCategories)

	override suspend fun getAllItemSubCategories(): List<ItemSubCategory> =
		itemSubCategoriesDao.getAll()

	override suspend fun getItemSubCategoryById(id: Int): ItemSubCategory =
		itemSubCategoriesDao.getById(id)

	companion object {
		@Volatile
		private var instance: ItemDatabaseRepositoryImpl? = null

		@JvmStatic
		fun getInstance(application: Application) =
			instance ?: synchronized(this) {
				instance ?: ItemDatabaseRepositoryImpl(application).also { instance = it }
			}
	}
}