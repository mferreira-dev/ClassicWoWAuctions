package pt.mferreira.classicauctions.data.models

data class ClassIndexResponse(
	var classes: List<Class>
) {
	data class Class(
		var name: String,
		var id: Int
	)
}