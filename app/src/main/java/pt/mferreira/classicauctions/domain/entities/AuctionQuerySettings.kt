package pt.mferreira.classicauctions.domain.entities

import pt.mferreira.classicauctions.data.entities.Realm

data class AuctionQuerySettings(
	val selectedRealm: Realm,
	val selectedVersion: Int,
	val selectedFaction: Int,
	val factionAuctionHouseId: Int,
	val namespace: String,
	val clusterId: Int,
	val region: String
)