package pt.mferreira.classicauctions.data.repositories

import pt.mferreira.classicauctions.data.models.ItemCategoriesResponse
import pt.mferreira.classicauctions.data.models.ItemMediaResponse
import pt.mferreira.classicauctions.data.models.ItemSearchResponse
import pt.mferreira.classicauctions.data.models.ItemSubCategoriesResponse
import pt.mferreira.classicauctions.data.network.Endpoints
import pt.mferreira.classicauctions.domain.repositories.ItemHttpRepository
import pt.mferreira.classicauctions.domain.usecases.LocalPreferencesUseCase

class ItemHttpRepositoryImpl(private val endpoints: Endpoints) : ItemHttpRepository {
	private val localPreferencesUseCase =
		LocalPreferencesUseCase(LocalPreferencesRepositoryImpl.getInstance())

	override suspend fun getItemCategories(): ItemCategoriesResponse {
		return endpoints.getItemCategories(localPreferencesUseCase.getStaticProgEu())
	}

	override suspend fun getItemSubCategories(categoryId: Int): ItemSubCategoriesResponse {
		return endpoints.getItemSubCategories(categoryId, localPreferencesUseCase.getStaticProgEu())
	}

	override suspend fun itemSearch(keyword: String): ItemSearchResponse {
		return endpoints.itemSearch(keyword, 1000, localPreferencesUseCase.getStaticProgEu())
	}

	override suspend fun getItemMedia(itemId: Int): ItemMediaResponse {
		return endpoints.getItemMedia(itemId, localPreferencesUseCase.getStaticProgEu())
	}

	companion object {
		@Volatile
		private var instance: ItemHttpRepositoryImpl? = null

		@JvmStatic
		fun getInstance(endpoints: Endpoints) =
			instance ?: synchronized(this) {
				instance ?: ItemHttpRepositoryImpl(endpoints).also { instance = it }
			}
	}
}