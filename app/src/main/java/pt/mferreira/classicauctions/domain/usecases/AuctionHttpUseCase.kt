package pt.mferreira.classicauctions.domain.usecases

import pt.mferreira.classicauctions.data.models.AuctionsResponse
import pt.mferreira.classicauctions.domain.repositories.AuctionHttpRepository
import pt.mferreira.classicauctions.utils.Either
import pt.mferreira.classicauctions.utils.Failure
import pt.mferreira.classicauctions.utils.NoConnectivityException
import retrofit2.HttpException

class AuctionHttpUseCase(private val repository: AuctionHttpRepository) {
	suspend fun getAuctions(
		region: String,
		realmId: Int,
		auctionHouseId: Int,
		namespace: String
	): Either<Failure, AuctionsResponse> {
		return try {
			val response = repository.getAuctions(region, realmId, auctionHouseId, namespace)
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