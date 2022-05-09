package pt.mferreira.classicauctions.presentation.pvp.teams

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import pt.mferreira.classicauctions.R
import pt.mferreira.classicauctions.data.models.LeaderboardDetailsResponse.Entry

class PvPTeamsAdapter(
	private val ctx: Context,
	private val teams: List<Entry>,
	private val fragmentManager: FragmentManager
) : RecyclerView.Adapter<PvPTeamsAdapter.TeamsViewHolder>() {

	var selectedIndex: Int = -1

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): PvPTeamsAdapter.TeamsViewHolder {
		return TeamsViewHolder(
			LayoutInflater.from(parent.context).inflate(R.layout.row_pvp_team, parent, false)
		)
	}

	override fun onBindViewHolder(holder: PvPTeamsAdapter.TeamsViewHolder, position: Int) {
		holder.bind(teams[position])
		holder.cardView.setOnClickListener {
			selectedIndex = holder.adapterPosition
			notifyDataSetChanged()

			if (teams[selectedIndex].team.members != null) {
				val fragment = PvPTeamsDetailsBottomSheet.newInstance(teams[selectedIndex])
				fragment.show(fragmentManager, "pvp_team_details")
			} else
				Toast.makeText(ctx, R.string.no_members, Toast.LENGTH_SHORT).show()

			true
		}
	}

	override fun getItemCount(): Int {
		return teams.size
	}

	inner class TeamsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val cardView = itemView.findViewById(R.id.row_pvp_team_card) as CardView
		private val name = itemView.findViewById(R.id.row_pvp_team_name_label) as TextView
		private val labelRealm = itemView.findViewById(R.id.row_pvp_team_realm_label) as TextView
		private val labelRating = itemView.findViewById(R.id.row_pvp_team_rating_label) as TextView
		private val factionIcon = itemView.findViewById(R.id.row_pvp_team_faction_icon) as ImageView
		private val labelGames = itemView.findViewById(R.id.row_pvp_team_games_label) as TextView
		private val labelWins = itemView.findViewById(R.id.row_pvp_team_wins_label) as TextView
		private val labelLosses = itemView.findViewById(R.id.row_pvp_team_losses_label) as TextView

		fun bind(entry: Entry) {
			name.text = entry.team.name
			labelRealm.text = entry.team.realm.name.capitalize()
			labelRating.text = entry.rating.toString()

			entry.faction?.let {
				when (it.name) {
					ctx.getString(R.string.alliance_upper) -> factionIcon.setImageResource(R.drawable.alliance)
					ctx.getString(R.string.horde_upper) -> factionIcon.setImageResource(R.drawable.horde)
				}
			}

			labelGames.text =
				ctx.getString(R.string.team_games_played, entry.seasonMatchStatistics.played)
			labelWins.text = ctx.getString(R.string.team_wins, entry.seasonMatchStatistics.won)
			labelLosses.text = ctx.getString(R.string.team_losses, entry.seasonMatchStatistics.lost)
		}
	}
}