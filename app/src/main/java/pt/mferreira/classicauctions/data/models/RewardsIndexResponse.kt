package pt.mferreira.classicauctions.data.models

import com.google.gson.annotations.SerializedName

data class RewardsIndexResponse(
	val season: Season,
	val rewards: List<Reward>
) {
	data class Season(
		val id: Int
	)

	data class Reward(
		val bracket: Bracket,
		val achievement: Achievement,
		@SerializedName("rating_cutoff") val ratingCutoff: Int
	) {
		data class Bracket(
			val id: Int,
			@SerializedName("type") val name: String
		)

		data class Achievement(
			val name: String,
			val id: Int,
		)
	}
}