package pt.mferreira.classicauctions.presentation.auctions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pt.mferreira.classicauctions.ClassicAuctions
import pt.mferreira.classicauctions.R
import pt.mferreira.classicauctions.data.models.Auction
import pt.mferreira.classicauctions.data.models.ItemSearchResponse
import pt.mferreira.classicauctions.data.network.NetworkClient
import pt.mferreira.classicauctions.data.repositories.ItemHttpRepositoryImpl
import pt.mferreira.classicauctions.domain.entities.AuctionRealmData
import pt.mferreira.classicauctions.domain.usecases.ItemHttpUseCase

class AuctionsAdapter(
	private val auctions: List<Auction>,
	private val itemResults: List<ItemSearchResponse.Result>,
	private val auctionRealmData: AuctionRealmData
) : RecyclerView.Adapter<AuctionsAdapter.AuctionsViewHolder>() {

	private val itemHttpUseCase =
		ItemHttpUseCase(ItemHttpRepositoryImpl.getInstance(NetworkClient.getWoWClient))

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): AuctionsAdapter.AuctionsViewHolder {
		return AuctionsViewHolder(
			LayoutInflater.from(parent.context).inflate(R.layout.row_auction, parent, false)
		)
	}

	override fun onBindViewHolder(holder: AuctionsAdapter.AuctionsViewHolder, position: Int) {
		holder.bind(auctions[position])
	}

	override fun getItemCount(): Int {
		return auctions.size
	}

	inner class AuctionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		private val imgAuctionMedia =
			itemView.findViewById(R.id.row_auction_media_icon) as ImageView
		private val labelAuctionName =
			itemView.findViewById(R.id.row_auction_name_label) as TextView
		private val labelAuctionCategory =
			itemView.findViewById(R.id.row_auction_category_label) as TextView

		private val labelAuctionBidGold =
			itemView.findViewById(R.id.row_auction_bid_gold_value) as TextView
		private val labelAuctionBidSilver =
			itemView.findViewById(R.id.row_auction_bid_silver_value) as TextView
		private val labelAuctionBidCopper =
			itemView.findViewById(R.id.row_auction_bid_copper_value) as TextView

		private val labelAuctionBuyoutGold =
			itemView.findViewById(R.id.row_auction_buyout_gold_value) as TextView
		private val labelAuctionBuyoutSilver =
			itemView.findViewById(R.id.row_auction_buyout_silver_value) as TextView
		private val labelAuctionBuyoutCopper =
			itemView.findViewById(R.id.row_auction_buyout_copper_value) as TextView

		private val labelAuctionDuration =
			itemView.findViewById(R.id.row_auction_duration_label) as TextView

		private val labelAuctionRealmName =
			itemView.findViewById(R.id.row_auction_label_realm_name) as TextView
		private val labelAuctionRealmDetails =
			itemView.findViewById(R.id.row_auction_label_realm_details) as TextView

		fun bind(auction: Auction) {
			val item = itemResults.single { it.data.id == auction.item.id }

			GlobalScope.launch {
				itemHttpUseCase.getItemMedia(item.data.media.id).either(
					success = {
						val icon = it.assets.filter { it.key == "icon" }
						imgAuctionMedia.load(icon.first().url)
					},
					failure = {}
				)
			}

			labelAuctionName.text = item.data.name.enUs

			val mainClass = item.data.itemClass.name.enUs
			val subClass = item.data.itemSubClass.name.enUs

			labelAuctionCategory.text = mainClass
			if (subClass != mainClass) {
				labelAuctionCategory.text =
					ClassicAuctions.applicationContext().getString(
						R.string.item_category_concat,
						labelAuctionCategory.text,
						subClass
					)
			}

			labelAuctionBidGold.text = getGold(auction.bid).toString()
			labelAuctionBidSilver.text = getSilver(auction.bid).toString()
			labelAuctionBidCopper.text = getCopper(auction.bid).toString()

			labelAuctionBuyoutGold.text = getGold(auction.buyout).toString()
			labelAuctionBuyoutSilver.text = getSilver(auction.buyout).toString()
			labelAuctionBuyoutCopper.text = getCopper(auction.buyout).toString()

			labelAuctionDuration.text = ClassicAuctions.applicationContext()
				.getString(R.string.item_duration_concat, auction.timeLeft.replace("_", " "))

			labelAuctionRealmName.text = "${auctionRealmData.name} [${auctionRealmData.version}]"
			labelAuctionRealmDetails.text =
				"${auctionRealmData.region.uppercase()} ${auctionRealmData.faction}"
		}

		private fun getGold(value: Int): Int {
			return value / 10000
		}

		private fun getSilver(value: Int): Int {
			var localValue = value
			val gold = localValue / 10000
			localValue -= gold * 10000
			return localValue / 100
		}

		private fun getCopper(value: Int): Int {
			var localValue = value
			val gold = localValue / 10000
			localValue -= gold * 10000
			val silver = localValue / 100
			localValue -= silver * 100
			return localValue
		}
	}
}