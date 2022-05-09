package pt.mferreira.classicauctions.data.models

import com.google.gson.annotations.SerializedName

data class ClassMediaResponse(
	val assets: List<Asset>
) {
	data class Asset(
		@SerializedName("value") val url: String,
	)
}