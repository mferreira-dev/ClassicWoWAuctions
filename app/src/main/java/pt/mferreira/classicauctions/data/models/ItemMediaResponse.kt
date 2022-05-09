package pt.mferreira.classicauctions.data.models

import com.google.gson.annotations.SerializedName

data class ItemMediaResponse(
	val assets: List<Asset>
) {
	data class Asset(
		val key: String,
		@SerializedName("value") val url: String
	)
}