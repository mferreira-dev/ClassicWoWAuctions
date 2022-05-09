package pt.mferreira.classicauctions.data.repositories

import pt.mferreira.classicauctions.ClassicAuctions
import pt.mferreira.classicauctions.R
import pt.mferreira.classicauctions.data.models.RealmDetailsResponse
import pt.mferreira.classicauctions.data.models.RealmIndexResponse
import pt.mferreira.classicauctions.data.network.Endpoints
import pt.mferreira.classicauctions.domain.repositories.RealmHttpRepository
import pt.mferreira.classicauctions.utils.extensions.eu

class RealmHttpRepositoryImpl(private val endpoints: Endpoints) : RealmHttpRepository {
	override suspend fun getRealmIndex(region: String, namespace: String): RealmIndexResponse {
		return if (region == String.eu())
			endpoints.getRealmIndex(
				ClassicAuctions.applicationContext().getString(R.string.realm_index_eu_url),
				namespace
			)
		else
			endpoints.getRealmIndex(
				ClassicAuctions.applicationContext().getString(R.string.realm_index_us_url),
				namespace
			)
	}

	override suspend fun getRealmDetails(
		region: String,
		realmId: Int,
		namespace: String,
	): RealmDetailsResponse {
		return if (region == String.eu())
			endpoints.getRealmDetails(
				ClassicAuctions.applicationContext().getString(R.string.realm_id_eu_url, realmId),
				namespace
			)
		else
			endpoints.getRealmDetails(
				ClassicAuctions.applicationContext()
					.getString(R.string.realm_id_us_url, realmId),
				namespace
			)
	}

	companion object {
		@Volatile
		private var instance: RealmHttpRepositoryImpl? = null

		@JvmStatic
		fun getInstance(endpoints: Endpoints) =
			instance ?: synchronized(this) {
				instance ?: RealmHttpRepositoryImpl(endpoints).also { instance = it }
			}
	}
}