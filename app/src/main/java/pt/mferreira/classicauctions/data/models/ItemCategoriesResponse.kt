package pt.mferreira.classicauctions.data.models

import com.google.gson.annotations.SerializedName

data class ItemCategoriesResponse(
	@SerializedName("item_classes") val categories: List<ItemCategory>
) {
	data class ItemCategory(
		val name: String,
		val id: Int
	)
}