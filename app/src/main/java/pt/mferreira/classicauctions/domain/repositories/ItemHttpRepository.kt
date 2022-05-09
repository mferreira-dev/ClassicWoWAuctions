package pt.mferreira.classicauctions.domain.repositories

import pt.mferreira.classicauctions.data.models.ItemCategoriesResponse
import pt.mferreira.classicauctions.data.models.ItemMediaResponse
import pt.mferreira.classicauctions.data.models.ItemSearchResponse
import pt.mferreira.classicauctions.data.models.ItemSubCategoriesResponse

interface ItemHttpRepository {
	suspend fun getItemCategories(): ItemCategoriesResponse
	suspend fun getItemSubCategories(categoryId: Int): ItemSubCategoriesResponse
	suspend fun itemSearch(keyword: String): ItemSearchResponse
	suspend fun getItemMedia(itemId: Int): ItemMediaResponse
}