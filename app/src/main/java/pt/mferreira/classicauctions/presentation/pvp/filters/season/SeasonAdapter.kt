package pt.mferreira.classicauctions.presentation.pvp.filters.season

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

class SeasonAdapter(
	private val context: Context,
	private val seasons: List<Int>
) : RecyclerView.Adapter<SeasonAdapter.SeasonViewHolder>() {

	var selectedIndex: Int = -1

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonViewHolder {
		return SeasonViewHolder(
			LayoutInflater.from(parent.context).inflate(R.layout.row_season, parent, false)
		)
	}

	override fun onBindViewHolder(holder: SeasonViewHolder, position: Int) {
		holder.bind(seasons[position])
		holder.cardView.setOnLongClickListener {
			selectedIndex = holder.adapterPosition
			notifyDataSetChanged()
			true
		}
		holder.handleColors(holder.adapterPosition)
	}

	override fun getItemCount(): Int {
		return seasons.size
	}

	inner class SeasonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val cardView = itemView.findViewById(R.id.row_season_card) as CardView
		private val labelName = itemView.findViewById(R.id.row_season_name_label) as TextView

		fun bind(season: Int) {
			labelName.text = context.getString(R.string.season_id, season)
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