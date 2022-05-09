package pt.mferreira.classicauctions.presentation.pvp.teams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.mferreira.classicauctions.R
import pt.mferreira.classicauctions.databinding.FragmentPvpTeamsBinding
import pt.mferreira.classicauctions.domain.entities.BracketsWrapper.Bracket
import pt.mferreira.classicauctions.presentation.common.BaseFragment
import pt.mferreira.classicauctions.utils.extensions.hideKeyboard
import java.text.SimpleDateFormat
import java.util.*

class PvPTeamsFragment : BaseFragment() {
	companion object {
		private const val PVP_TEAMS_BRACKET = "pvp_teams_parameters"
		private const val PVP_TEAMS_SEASON_START = "pvp_teams_season_start"
		private const val PVP_TEAMS_SEASON_END = "pvp_teams_season_end"

		fun newInstance(bracket: Bracket, dates: Pair<Long, Long>) =
			PvPTeamsFragment().apply {
				arguments = Bundle().apply {
					putParcelable(PVP_TEAMS_BRACKET, bracket)
					putLong(PVP_TEAMS_SEASON_START, dates.first)
					putLong(PVP_TEAMS_SEASON_END, dates.second)
				}
			}
	}

	private var _binding: FragmentPvpTeamsBinding? = null
	private val binding get() = _binding!!

	private lateinit var viewModel: PvPTeamsViewModel

	private lateinit var teamsRecyclerView: RecyclerView
	private lateinit var teamsAdapter: PvPTeamsAdapter

	private lateinit var bracket: Bracket
	private var startDate = 0L
	private var endDate = 0L

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		arguments?.getParcelable<Bracket>(PVP_TEAMS_BRACKET)?.let {
			bracket = it
		}

		arguments?.getLong(PVP_TEAMS_SEASON_START)?.let {
			startDate = it
		}

		arguments?.getLong(PVP_TEAMS_SEASON_END)?.let {
			endDate = it
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentPvpTeamsBinding.inflate(inflater, container, false)
		viewModel = ViewModelProvider(this).get(PvPTeamsViewModel::class.java)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setupButtons()
		setupObservers()
		setupUI()
		setupStrings()
	}

	override fun setupUI() {
		teamsRecyclerView = binding.teamsList
		teamsRecyclerView.layoutManager = LinearLayoutManager(context)
		viewModel.updateLeaderboard(bracket)
	}

	override fun setupButtons() {
		binding.teamsFab.setOnClickListener {
			teamsRecyclerView.smoothScrollToPosition(0)
		}
	}

	override fun setupObservers() {
		viewModel.pvpTeamsViewState.observe(viewLifecycleOwner, {
			render(it)
			binding.fragmentPvpTeamsLayout.hideKeyboard()
		})

		viewModel.leaderboard.observe(viewLifecycleOwner, {
			teamsAdapter =
				PvPTeamsAdapter(requireContext(), it, requireActivity().supportFragmentManager)
			teamsRecyclerView.adapter = teamsAdapter
		})
	}

	override fun setupStrings() {
		val formatter = SimpleDateFormat("yyyy/MM/dd")
		val startDateString = formatter.format(Date(startDate))
		val endDateString = formatter.format(Date(endDate))

		if (endDate == 0L)
			binding.bracketTitle.text = requireContext().getString(
				R.string.bracket_and_start,
				bracket.name,
				startDateString
			)
		else
			binding.bracketTitle.text = requireContext().getString(
				R.string.bracket_start_end,
				bracket.name,
				startDateString,
				endDateString
			)
	}

	private fun render(viewState: PvPTeamsViewModel.PvPTeamsViewState) {
		binding.lblLoadingFromCache.visibility = viewState.loadingFromCache
		binding.lblLoadingFromServer.visibility = viewState.loadingFromServer

		if (viewState.loadingFromCache == VISIBLE || viewState.loadingFromServer == VISIBLE) {
			binding.pvpLoading.visibility = VISIBLE

			activity?.window?.setFlags(
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
			)
		} else {
			binding.pvpLoading.visibility = GONE
			activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}