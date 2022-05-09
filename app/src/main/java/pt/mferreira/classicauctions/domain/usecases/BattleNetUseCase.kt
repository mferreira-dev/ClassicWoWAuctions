package pt.mferreira.classicauctions.domain.usecases

import pt.mferreira.classicauctions.data.models.OAuthTokenResponse
import pt.mferreira.classicauctions.domain.repositories.BattleNetRepository
import pt.mferreira.classicauctions.utils.Either
import pt.mferreira.classicauctions.utils.Failure
import pt.mferreira.classicauctions.utils.NoConnectivityException
import retrofit2.HttpException

class BattleNetUseCase(private val repository: BattleNetRepository) {
	suspend fun getOAuthToken(): Either<Failure, OAuthTokenResponse> {
		return try {
			val response = repository.getOAuthToken()
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