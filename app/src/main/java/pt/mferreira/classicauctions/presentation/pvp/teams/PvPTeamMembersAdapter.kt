package pt.mferreira.classicauctions.presentation.pvp.teams

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pt.mferreira.classicauctions.R
import pt.mferreira.classicauctions.data.models.LeaderboardDetailsResponse.Entry
import pt.mferreira.classicauctions.data.models.LeaderboardDetailsResponse.Entry.Team.Member
import pt.mferreira.classicauctions.data.repositories.ClassDatabaseRepositoryImpl
import pt.mferreira.classicauctions.domain.usecases.ClassDatabaseUseCase

class PvPTeamMembersAdapter(
	private val ctx: Context,
	app: Application,
	private val entry: Entry
) : RecyclerView.Adapter<PvPTeamMembersAdapter.TeamMembersViewHolder>() {

	private val classDatabaseUseCase = ClassDatabaseUseCase(
		ClassDatabaseRepositoryImpl.getInstance(app)
	)

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): PvPTeamMembersAdapter.TeamMembersViewHolder {
		return TeamMembersViewHolder(
			LayoutInflater.from(parent.context).inflate(R.layout.row_pvp_team_member, parent, false)
		)
	}

	override fun onBindViewHolder(
		holder: PvPTeamMembersAdapter.TeamMembersViewHolder,
		position: Int
	) {
		holder.bind(entry.team.members[position])
	}

	override fun getItemCount(): Int {
		return if (entry.team.members != null)
			entry.team.members.size
		else
			0
	}

	inner class TeamMembersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		private val raceIcon = itemView.findViewById(R.id.row_member_race_icon) as ImageView
		private val classIcon = itemView.findViewById(R.id.row_member_class_icon) as ImageView
		private val labelMemberName = itemView.findViewById(R.id.row_member_name_label) as TextView
		private val labelGames = itemView.findViewById(R.id.row_member_games_label) as TextView
		private val labelWins = itemView.findViewById(R.id.row_member_wins_label) as TextView
		private val labelLosses = itemView.findViewById(R.id.row_member_losses_label) as TextView

		fun bind(member: Member) {
			when (member.character.race.id) {
				1 -> raceIcon.setImageResource(R.drawable.human)
				2 -> raceIcon.setImageResource(R.drawable.orc)
				3 -> raceIcon.setImageResource(R.drawable.dwarf)
				4 -> raceIcon.setImageResource(R.drawable.night_elf)
				5 -> raceIcon.setImageResource(R.drawable.undead)
				6 -> raceIcon.setImageResource(R.drawable.tauren)
				7 -> raceIcon.setImageResource(R.drawable.gnome)
				8 -> raceIcon.setImageResource(R.drawable.troll)
				10 -> raceIcon.setImageResource(R.drawable.blood_elf)
				11 -> raceIcon.setImageResource(R.drawable.draenei)
			}

			GlobalScope.launch {
				val aClass = classDatabaseUseCase.getById(member.character.aClass.id.toString())
				aClass?.let {
					classIcon.load(aClass.icon)
				}
			}

			labelMemberName.text = member.character.name

			labelGames.text =
				ctx.getString(R.string.team_games_played, member.seasonMatchStatistics.played)
			labelWins.text = ctx.getString(R.string.team_wins, member.seasonMatchStatistics.won)
			labelLosses.text =
				ctx.getString(R.string.team_losses, member.seasonMatchStatistics.lost)
		}
	}
}