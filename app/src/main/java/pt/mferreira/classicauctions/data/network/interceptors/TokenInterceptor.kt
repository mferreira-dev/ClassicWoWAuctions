package pt.mferreira.classicauctions.data.network.interceptors

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import pt.mferreira.classicauctions.data.network.NetworkClient
import pt.mferreira.classicauctions.data.repositories.BattleNetRepositoryImpl
import pt.mferreira.classicauctions.data.repositories.LocalPreferencesRepositoryImpl
import pt.mferreira.classicauctions.domain.usecases.BattleNetUseCase
import pt.mferreira.classicauctions.domain.usecases.LocalPreferencesUseCase

/**
 * This class checks if the authentication token has expired before EVERY request. If it has in fact
 * expired then it refreshes it before proceeding with the other request.
 *
 * Because of the way Blizzard developed their API we can't use an Authenticator to refresh
 * the token. An Authenticator waits for a 401 request (invalid authentication header) before
 * refreshing, which means it could potentially throttle the app.
 * Furthermore, this API uses the authentication token as a parameter rather than a header, which
 * means a 403 error is received instead of a 401.
 */
class TokenInterceptor : Interceptor {
	private val localPreferencesUseCase =
		LocalPreferencesUseCase(LocalPreferencesRepositoryImpl.getInstance())

	override fun intercept(chain: Interceptor.Chain): Response {
		return runBlocking {
			val expirationDate = localPreferencesUseCase.getTokenExpirationDate()
			if (expirationDate == 0L || expirationDate < System.currentTimeMillis())
				getOAuthToken()

			val url = chain.request().url
				.newBuilder()
				.addQueryParameter("locale", "en_US")
				.addQueryParameter(
					"access_token",
					localPreferencesUseCase.getToken()
				)
				.build()

			chain.proceed(chain.request().newBuilder().url(url).build())
		}
	}

	private suspend fun getOAuthToken() {
		BattleNetUseCase(BattleNetRepositoryImpl.getInstance(NetworkClient.getBattleNetClient)).getOAuthToken()
			.either(
				success = {
					localPreferencesUseCase.setToken(it.accessToken)
					localPreferencesUseCase.setTokenExpirationDate(System.currentTimeMillis() + it.expiresIn)
				},
				failure = {}
			)
	}
}