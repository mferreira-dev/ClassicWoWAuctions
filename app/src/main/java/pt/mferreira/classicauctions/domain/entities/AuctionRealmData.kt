package pt.mferreira.classicauctions.domain.entities

import pt.mferreira.classicauctions.utils.extensions.empty

data class AuctionRealmData(
	var name: String = String.empty(),
	var version: String = String.empty(),
	var region: String = String.empty(),
	var faction: String = String.empty()
)