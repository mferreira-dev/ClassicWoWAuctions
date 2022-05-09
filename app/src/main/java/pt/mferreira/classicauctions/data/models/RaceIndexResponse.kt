package pt.mferreira.classicauctions.data.models

data class RaceIndexResponse(
	var races: List<Race>
) {
	data class Race(
		var name: String,
		var id: Int
	)
}