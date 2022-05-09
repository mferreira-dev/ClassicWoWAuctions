package pt.mferreira.classicauctions.domain.repositories

import pt.mferreira.classicauctions.data.models.RealmDetailsResponse
import pt.mferreira.classicauctions.data.models.RealmIndexResponse

interface RealmHttpRepository {
	suspend fun getRealmIndex(region: String, namespace: String): RealmIndexResponse

	suspend fun getRealmDetails(
		region: String,
		realmId: Int,
		namespace: String
	): RealmDetailsResponse
}