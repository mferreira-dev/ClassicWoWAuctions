package pt.mferreira.classicauctions.data.models

data class LeaderboardIndexResponse(
	val leaderboards: List<Leaderboard>
) {
	data class Leaderboard(
		val name: String,
		val id: Int
	)
}