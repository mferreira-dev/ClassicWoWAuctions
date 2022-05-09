package pt.mferreira.classicauctions.data.models

import com.google.gson.annotations.SerializedName

data class PvPRegionSeasonDetailsResponse(
	@SerializedName("season_start_timestamp") val seasonStart: Long,
	@SerializedName("season_end_timestamp") val seasonEnd: Long? = 0L
)