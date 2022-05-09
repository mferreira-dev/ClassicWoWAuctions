package pt.mferreira.classicauctions.data.models

import com.google.gson.annotations.SerializedName

data class PvPRegionSeasonIndexResponse(
	val seasons: List<Season>,
	@SerializedName("current_season") val currentSeason: Season
) {
	data class Season(
		val id: Int
	)
}