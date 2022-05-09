package pt.mferreira.classicauctions.data.models

import com.google.gson.annotations.SerializedName

data class ItemSearchResponse(
	val results: List<Result>
) {
	data class Result(
		val data: Data
	) {
		data class Data(
			@SerializedName("required_level") val requiredLevel: Int,
			@SerializedName("sell_price") val sellPrice: Int,
			@SerializedName("item_subclass") val itemSubClass: ItemSubClass,
			@SerializedName("is_equippable") val isEquippable: Boolean,
			val media: Media,
			@SerializedName("item_class") val itemClass: ItemClass,
			val quality: Quality,
			@SerializedName("max_count") val maxCount: Int,
			@SerializedName("is_stackable") val isStackable: Boolean,
			val name: Names,
			@SerializedName("purchase_price") val purchasePrice: Int,
			val id: Int,
			@SerializedName("inventory_type") val inventoryType: InventoryType
		) {
			data class ItemSubClass(
				val name: Names,
				val id: Int,
			)

			data class Media(
				val id: Int
			)

			data class ItemClass(
				val name: Names,
				val id: Int,
			)

			data class Quality(
				val name: Names,
				val type: String
			)

			data class InventoryType(
				val name: Names,
				val type: String
			)
		}
	}
}

data class Names(
	@SerializedName("en_US") val enUs: String,
)