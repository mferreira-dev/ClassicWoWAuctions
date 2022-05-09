package pt.mferreira.classicauctions.data.models

import com.google.gson.annotations.SerializedName

data class Auction(
	val id: Int,
	val item: Item,
	val bid: Int,
	val buyout: Int,
	val quantity: Int,
	@SerializedName("time_left") val timeLeft: String
) {
	data class Item(
		val id: Int,
		val rand: Int,
		val seed: Int
	)
}