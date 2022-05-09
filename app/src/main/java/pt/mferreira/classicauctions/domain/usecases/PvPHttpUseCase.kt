package pt.mferreira.classicauctions.domain.usecases

import pt.mferreira.classicauctions.data.models.*
import pt.mferreira.classicauctions.domain.repositories.PvPHttpRepository
import pt.mferreira.classicauctions.utils.Either
import pt.mferreira.classicauctions.utils.Failure
import pt.mferreira.classicauctions.utils.NoConnectivityException
import retrofit2.HttpException

class PvPHttpUseCase(private val repository: PvPHttpRepository) {
	suspend fun getPvPRegionIndex(
		region: String
	): Either<Failure, PvPRegionIndexResponse> {
		return try {
			val response = repository.getPvPRegionIndex(region)
			Either.Success(response)
		} catch (ex: NoConnectivityException) {
			Either.Failure(Failure.NotConnected)
		} catch (ex: HttpException) {
			Either.Failure(Failure.ServerError)
		} catch (ex: Exception) {
			Either.Failure(Failure.Other)
		}
	}

	suspend fun getPvPRegionSeasonIndex(
		pvpRegionId: Int,
		region: String
	): Either<Failure, PvPRegionSeasonIndexResponse> {
		return try {
			val response = repository.getPvPRegionSeasonIndex(pvpRegionId, region)
			Either.Success(response)
		} catch (ex: NoConnectivityException) {
			Either.Failure(Failure.NotConnected)
		} catch (ex: HttpException) {
			Either.Failure(Failure.ServerError)
		} catch (ex: Exception) {
			Either.Failure(Failure.Other)
		}
	}

	suspend fun getPvPRegionSeasonDetails(
		pvpRegionId: Int,
		seasonId: Int,
		region: String
	): Either<Failure, PvPRegionSeasonDetailsResponse> {
		return try {
			val response =
				repository.getPvPRegionSeasonDetails(pvpRegionId, seasonId, region)
			Either.Success(response)
		} catch (ex: NoConnectivityException) {
			Either.Failure(Failure.NotConnected)
		} catch (ex: HttpException) {
			Either.Failure(Failure.ServerError)
		} catch (ex: Exception) {
			Either.Failure(Failure.Other)
		}
	}

	suspend fun getLeaderboardIndex(
		pvpRegionId: Int,
		seasonId: Int,
		region: String
	): Either<Failure, LeaderboardIndexResponse> {
		return try {
			val response =
				repository.getLeaderboardIndex(pvpRegionId, seasonId, region)
			Either.Success(response)
		} catch (ex: NoConnectivityException) {
			Either.Failure(Failure.NotConnected)
		} catch (ex: HttpException) {
			Either.Failure(Failure.ServerError)
		} catch (ex: Exception) {
			Either.Failure(Failure.Other)
		}
	}

	suspend fun getLeaderboardDetails(
		pvpRegionId: Int,
		seasonId: Int,
		bracket: String,
		region: String
	): Either<Failure, LeaderboardDetailsResponse> {
		return try {
			val response =
				repository.getLeaderboardDetails(pvpRegionId, seasonId, bracket, region)
			Either.Success(response)
		} catch (ex: NoConnectivityException) {
			Either.Failure(Failure.NotConnected)
		} catch (ex: HttpException) {
			Either.Failure(Failure.ServerError)
		} catch (ex: Exception) {
			Either.Failure(Failure.Other)
		}
	}

	suspend fun getRewardsIndex(
		pvpRegionId: Int,
		seasonId: Int,
		region: String
	): Either<Failure, RewardsIndexResponse> {
		return try {
			val response =
				repository.getRewardsIndex(pvpRegionId, seasonId, region)
			Either.Success(response)
		} catch (ex: NoConnectivityException) {
			Either.Failure(Failure.NotConnected)
		} catch (ex: HttpException) {
			Either.Failure(Failure.ServerError)
		} catch (ex: Exception) {
			Either.Failure(Failure.Other)
		}
	}
}