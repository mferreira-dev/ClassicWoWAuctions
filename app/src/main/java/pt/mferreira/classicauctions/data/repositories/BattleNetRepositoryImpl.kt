package pt.mferreira.classicauctions.data.repositories

import pt.mferreira.classicauctions.data.models.OAuthTokenResponse
import pt.mferreira.classicauctions.data.network.Endpoints
import pt.mferreira.classicauctions.domain.repositories.BattleNetRepository

class BattleNetRepositoryImpl(private val endpoints: Endpoints) : BattleNetRepository {
	override suspend fun getOAuthToken(): OAuthTokenResponse {
		return endpoints.getOAuthToken()
	}

	companion object {
		@Volatile
		private var instance: BattleNetRepositoryImpl? = null

		@JvmStatic
		fun getInstance(endpoints: Endpoints) = instance ?: synchronized(this) {
			instance ?: BattleNetRepositoryImpl(endpoints).also { instance = it }
		}
	}
}