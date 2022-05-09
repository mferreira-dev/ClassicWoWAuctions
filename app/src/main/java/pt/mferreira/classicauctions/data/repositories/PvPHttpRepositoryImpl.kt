package pt.mferreira.classicauctions.data.repositories

import pt.mferreira.classicauctions.ClassicAuctions
import pt.mferreira.classicauctions.R
import pt.mferreira.classicauctions.data.models.*
import pt.mferreira.classicauctions.data.network.Endpoints
import pt.mferreira.classicauctions.domain.repositories.PvPHttpRepository
import pt.mferreira.classicauctions.utils.extensions.eu

class PvPHttpRepositoryImpl(private val endpoints: Endpoints) : PvPHttpRepository {
	private val dynamicProgEu =
		ClassicAuctions.applicationContext().getString(R.string.dynamic_prog_eu)
	private val dynamicProgUs =
		ClassicAuctions.applicationContext().getString(R.string.dynamic_prog_us)

	override suspend fun getPvPRegionIndex(
		region: String
	): PvPRegionIndexResponse {
		return if (region == String.eu())
			endpoints.getPvPRegionIndex(
				ClassicAuctions.applicationContext().getString(R.string.eu_pvp_region_index),
				dynamicProgEu
			)
		else
			endpoints.getPvPRegionIndex(
				ClassicAuctions.applicationContext().getString(R.string.us_pvp_region_index),
				dynamicProgUs
			)
	}

	override suspend fun getPvPRegionSeasonIndex(
		pvpRegionId: Int,
		region: String,
	): PvPRegionSeasonIndexResponse {
		return if (region == String.eu())
			endpoints.getPvPRegionSeasonIndex(
				ClassicAuctions.applicationContext()
					.getString(R.string.eu_pvp_region_season_index, pvpRegionId),
				dynamicProgEu
			)
		else
			endpoints.getPvPRegionSeasonIndex(
				ClassicAuctions.applicationContext()
					.getString(R.string.us_pvp_region_season_index, pvpRegionId),
				dynamicProgUs
			)
	}

	override suspend fun getPvPRegionSeasonDetails(
		pvpRegionId: Int,
		seasonId: Int,
		region: String,
	): PvPRegionSeasonDetailsResponse {
		return if (region == String.eu())
			endpoints.getPvPRegionSeasonDetails(
				ClassicAuctions.applicationContext()
					.getString(R.string.eu_pvp_region_season_details, pvpRegionId, seasonId),
				dynamicProgEu
			)
		else
			endpoints.getPvPRegionSeasonDetails(
				ClassicAuctions.applicationContext()
					.getString(R.string.us_pvp_region_season_details, pvpRegionId, seasonId),
				dynamicProgUs
			)
	}

	override suspend fun getLeaderboardIndex(
		pvpRegionId: Int,
		seasonId: Int,
		region: String,
	): LeaderboardIndexResponse {
		return if (region == String.eu())
			endpoints.getLeaderboardIndex(
				ClassicAuctions.applicationContext()
					.getString(R.string.eu_leaderboard_index, pvpRegionId, seasonId),
				dynamicProgEu
			)
		else
			endpoints.getLeaderboardIndex(
				ClassicAuctions.applicationContext()
					.getString(R.string.us_leaderboard_index, pvpRegionId, seasonId),
				dynamicProgUs
			)
	}

	override suspend fun getLeaderboardDetails(
		pvpRegionId: Int,
		seasonId: Int,
		bracket: String,
		region: String,
	): LeaderboardDetailsResponse {
		return if (region == String.eu())
			endpoints.getLeaderboardDetails(
				ClassicAuctions.applicationContext()
					.getString(R.string.eu_leaderboard_details, pvpRegionId, seasonId, bracket),
				dynamicProgEu
			)
		else
			endpoints.getLeaderboardDetails(
				ClassicAuctions.applicationContext()
					.getString(R.string.us_leaderboard_details, pvpRegionId, seasonId, bracket),
				dynamicProgUs
			)
	}

	override suspend fun getRewardsIndex(
		pvpRegionId: Int,
		seasonId: Int,
		region: String,
	): RewardsIndexResponse {
		return if (region == String.eu())
			endpoints.getRewardsIndex(
				ClassicAuctions.applicationContext()
					.getString(R.string.eu_rewards_index, pvpRegionId, seasonId),
				dynamicProgEu
			)
		else
			endpoints.getRewardsIndex(
				ClassicAuctions.applicationContext()
					.getString(R.string.us_rewards_index, pvpRegionId, seasonId),
				dynamicProgUs
			)
	}

	companion object {
		@Volatile
		private var instance: PvPHttpRepositoryImpl? = null

		@JvmStatic
		fun getInstance(endpoints: Endpoints) =
			instance ?: synchronized(this) {
				instance ?: PvPHttpRepositoryImpl(endpoints).also { instance = it }
			}
	}
}