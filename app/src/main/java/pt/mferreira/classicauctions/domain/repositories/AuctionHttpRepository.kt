package pt.mferreira.classicauctions.domain.repositories

import pt.mferreira.classicauctions.data.models.AuctionsResponse

interface AuctionHttpRepository {
	suspend fun getAuctions(
		region: String,
		realmId: Int,
		auctionHouseId: Int,
		namespace: String
	): AuctionsResponse
}