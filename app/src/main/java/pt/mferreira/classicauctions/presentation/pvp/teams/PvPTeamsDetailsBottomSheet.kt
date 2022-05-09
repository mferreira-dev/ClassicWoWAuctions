package pt.mferreira.classicauctions.presentation.pvp.teams

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_pvp_team_details.*
import pt.mferreira.classicauctions.ClassicAuctions
import pt.mferreira.classicauctions.R
import pt.mferreira.classicauctions.data.models.LeaderboardDetailsResponse.Entry

class PvPTeamsDetailsBottomSheet : BottomSheetDialogFragment() {
	companion object {
		private const val TEAM_DETAILS_PARAMETERS = "team_details_parameters"

		fun newInstance(entry: Entry) =
			PvPTeamsDetailsBottomSheet().apply {
				arguments = Bundle().apply {
					putParcelable(TEAM_DETAILS_PARAMETERS, entry)
				}
			}
	}

	private lateinit var membersRecyclerView: RecyclerView
	private lateinit var membersAdapter: PvPTeamMembersAdapter

	private lateinit var entry: Entry

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		arguments?.getParcelable<Entry>(TEAM_DETAILS_PARAMETERS)?.let {
			entry = it
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_pvp_team_details, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setupButtons()
		setupUI()
		setupStrings()
	}

	private fun setupButtons() {

	}

	private fun setupUI() {
		membersRecyclerView = member_list
		membersRecyclerView.layoutManager = LinearLayoutManager(context)
		membersAdapter =
			PvPTeamMembersAdapter(requireContext(), ClassicAuctions.instance as Application, entry)
		membersRecyclerView.adapter = membersAdapter
	}

	private fun setupStrings() {

	}
}