package pt.mferreira.classicauctions.data.models

import com.google.gson.annotations.SerializedName

data class PvPRegionIndexResponse(
	@SerializedName("pvp_regions") val pvpRegions: List<PvPRegion>
) {
	data class PvPRegion(
		val href: String
	)
}