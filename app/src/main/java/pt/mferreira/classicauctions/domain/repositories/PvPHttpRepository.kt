package pt.mferreira.classicauctions.domain.repositories

import pt.mferreira.classicauctions.data.models.*

interface PvPHttpRepository {
	suspend fun getPvPRegionIndex(region: String): PvPRegionIndexResponse

	suspend fun getPvPRegionSeasonIndex(
		pvpRegionId: Int,
		region: String,
	): PvPRegionSeasonIndexResponse

	suspend fun getPvPRegionSeasonDetails(
		pvpRegionId: Int,
		seasonId: Int,
		region: String,
	): PvPRegionSeasonDetailsResponse

	suspend fun getLeaderboardIndex(
		pvpRegionId: Int,
		seasonId: Int,
		region: String,
	): LeaderboardIndexResponse

	suspend fun getLeaderboardDetails(
		pvpRegionId: Int,
		seasonId: Int,
		bracket: String,
		region: String,
	): LeaderboardDetailsResponse

	suspend fun getRewardsIndex(
		pvpRegionId: Int,
		seasonId: Int,
		region: String,
	): RewardsIndexResponse
}