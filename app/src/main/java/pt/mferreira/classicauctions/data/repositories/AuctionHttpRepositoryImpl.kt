package pt.mferreira.classicauctions.data.repositories

import pt.mferreira.classicauctions.ClassicAuctions
import pt.mferreira.classicauctions.R
import pt.mferreira.classicauctions.data.models.AuctionsResponse
import pt.mferreira.classicauctions.data.network.Endpoints
import pt.mferreira.classicauctions.domain.repositories.AuctionHttpRepository
import pt.mferreira.classicauctions.utils.extensions.eu

class AuctionHttpRepositoryImpl(private val endpoints: Endpoints) : AuctionHttpRepository {
	override suspend fun getAuctions(
		region: String,
		realmId: Int,
		auctionHouseId: Int,
		namespace: String
	): AuctionsResponse {
		return if (region == String.eu())
			endpoints.getAuctions(
				ClassicAuctions.applicationContext()
					.getString(R.string.auctions_eu_url, realmId, auctionHouseId),
				namespace
			)
		else
			endpoints.getAuctions(
				ClassicAuctions.applicationContext()
					.getString(R.string.auctions_us_url, realmId, auctionHouseId),
				namespace
			)
	}

	companion object {
		@Volatile
		private var instance: AuctionHttpRepositoryImpl? = null

		@JvmStatic
		fun getInstance(endpoints: Endpoints) =
			instance ?: synchronized(this) {
				instance ?: AuctionHttpRepositoryImpl(endpoints).also { instance = it }
			}
	}
}