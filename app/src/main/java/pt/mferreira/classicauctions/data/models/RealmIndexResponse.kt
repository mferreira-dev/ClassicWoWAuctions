package pt.mferreira.classicauctions.data.models

import com.google.gson.annotations.SerializedName

data class RealmIndexResponse(
	@SerializedName("connected_realms") var realms: List<Realm>
) {
	data class Realm(
		var href: String
	)
}