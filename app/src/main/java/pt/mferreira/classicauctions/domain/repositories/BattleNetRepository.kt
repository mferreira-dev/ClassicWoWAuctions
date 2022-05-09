package pt.mferreira.classicauctions.domain.repositories

import pt.mferreira.classicauctions.data.models.OAuthTokenResponse

interface BattleNetRepository {
	suspend fun getOAuthToken(): OAuthTokenResponse
}