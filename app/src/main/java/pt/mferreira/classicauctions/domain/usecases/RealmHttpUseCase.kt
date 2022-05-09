package pt.mferreira.classicauctions.domain.usecases

import pt.mferreira.classicauctions.data.models.RealmDetailsResponse
import pt.mferreira.classicauctions.data.models.RealmIndexResponse
import pt.mferreira.classicauctions.domain.repositories.RealmHttpRepository
import pt.mferreira.classicauctions.utils.Either
import pt.mferreira.classicauctions.utils.Failure
import pt.mferreira.classicauctions.utils.NoConnectivityException
import retrofit2.HttpException

class RealmHttpUseCase(private val repository: RealmHttpRepository) {
	suspend fun getRealmIndex(
		region: String,
		namespace: String
	): Either<Failure, RealmIndexResponse> {
		return try {
			val response = repository.getRealmIndex(region, namespace)
			Either.Success(response)
		} catch (ex: NoConnectivityException) {
			Either.Failure(Failure.NotConnected)
		} catch (ex: HttpException) {
			Either.Failure(Failure.ServerError)
		} catch (ex: Exception) {
			Either.Failure(Failure.Other)
		}
	}

	suspend fun getRealmDetails(
		region: String,
		realmId: Int,
		namespace: String
	): Either<Failure, RealmDetailsResponse> {
		return try {
			val response = repository.getRealmDetails(region, realmId, namespace)
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