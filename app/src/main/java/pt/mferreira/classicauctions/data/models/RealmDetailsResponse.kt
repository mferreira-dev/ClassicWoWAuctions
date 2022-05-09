package pt.mferreira.classicauctions.data.models

data class RealmDetailsResponse(
	val id: Int,
	val realms: List<Realm>
) {
	data class Realm(
		val id: Int,
		val name: String,
		val category: String,
		val timezone: String,
		val type: Type
	) {
		data class Type(
			val name: String
		)
	}
}