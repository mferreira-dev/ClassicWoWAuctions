package pt.mferreira.classicauctions.data.models

data class AuctionsResponse(
	val auctions: List<Auction> = mutableListOf()
)