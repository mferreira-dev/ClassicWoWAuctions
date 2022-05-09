package pt.mferreira.classicauctions.presentation.pvp.filters.pvp_region

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import pt.mferreira.classicauctions.R
import pt.mferreira.classicauctions.data.entities.PvPEntry

class PvPRegionAdapter(
	private val context: Context,
	private val pvpEntries: List<PvPEntry>
) : RecyclerView.Adapter<PvPRegionAdapter.PvPRegionViewHolder>() {

	var selectedIndex: Int = -1

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PvPRegionViewHolder {
		return PvPRegionViewHolder(
			LayoutInflater.from(parent.context).inflate(R.layout.row_pvp_region, parent, false)
		)
	}

	override fun onBindViewHolder(holder: PvPRegionViewHolder, position: Int) {
		holder.bind(pvpEntries[position])
		holder.cardView.setOnLongClickListener {
			selectedIndex = holder.adapterPosition
			notifyDataSetChanged()
			true
		}
		holder.handleColors(holder.adapterPosition)
	}

	override fun getItemCount(): Int {
		return pvpEntries.size
	}

	inner class PvPRegionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val cardView = itemView.findViewById(R.id.row_pvp_region_card) as CardView
		private val labelName = itemView.findViewById(R.id.row_pvp_region_name_label) as TextView

		fun bind(pvpEntry: PvPEntry) {
			labelName.text = context.getString(R.string.pvp_region_id, pvpEntry.pvpRegionId)
		}

		fun handleColors(adapterPosition: Int) {
			val cardViewColor: Int
			val textColor: Int

			if (selectedIndex == adapterPosition) {
				val value = TypedValue()
				context.theme.resolveAttribute(android.R.attr.colorPrimary, value, true)

				cardViewColor = value.data
				textColor = ContextCompat.getColor(context, R.color.white)
			} else {
				cardViewColor = ContextCompat.getColor(context, R.color.white)
				textColor = ContextCompat.getColor(context, android.R.color.tab_indicator_text)
			}

			cardView.setCardBackgroundColor(cardViewColor)
			labelName.setTextColor(textColor)
		}
	}
}