package pt.mferreira.classicauctions.presentation.auctions.filters.realm

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
import pt.mferreira.classicauctions.data.entities.Realm

class RealmAdapter(private val context: Context, private val realms: List<Realm>) :
	RecyclerView.Adapter<RealmAdapter.RealmViewHolder>() {

	var selectedIndex: Int = -1

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RealmViewHolder {
		return RealmViewHolder(
			LayoutInflater.from(parent.context).inflate(R.layout.row_realm, parent, false)
		)
	}

	override fun onBindViewHolder(holder: RealmViewHolder, position: Int) {
		holder.bind(realms[position])
		holder.cardView.setOnLongClickListener {
			selectedIndex = holder.adapterPosition
			notifyDataSetChanged()
			true
		}
		holder.handleColors(holder.adapterPosition)
	}

	override fun getItemCount(): Int {
		return realms.size
	}

	inner class RealmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val cardView = itemView.findViewById(R.id.row_realm_card) as CardView
		private val labelName = itemView.findViewById(R.id.row_realm_name_label) as TextView
		private val labelType = itemView.findViewById(R.id.row_realm_type_label) as TextView

		fun bind(realm: Realm) {
			labelName.text =
				context.getString(R.string.realm_and_region, realm.name, realm.region.uppercase())

			if (realm.type.contains(" ")) {
				val split = realm.type.split(" ")
				labelType.text = context.getString(R.string.realm_type, split[1], split[0])
			} else
				labelType.text = realm.type
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
			labelType.setTextColor(textColor)
		}
	}
}