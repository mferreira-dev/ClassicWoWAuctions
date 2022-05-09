package pt.mferreira.classicauctions.data.models

import com.google.gson.annotations.SerializedName

data class ItemSubCategoriesResponse(
	@SerializedName("item_subclasses") val categories: List<ItemSubCategory> = mutableListOf()
) {
	data class ItemSubCategory(
		val name: String,
		val id: Int
	)
}