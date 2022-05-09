package pt.mferreira.classicauctions.presentation.pvp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import pt.mferreira.classicauctions.databinding.FragmentPvpBinding
import pt.mferreira.classicauctions.presentation.common.BaseFragment
import pt.mferreira.classicauctions.utils.extensions.hideKeyboard

class PvPFragment : BaseFragment() {
	private var _binding: FragmentPvpBinding? = null
	private val binding get() = _binding!!

	private lateinit var viewModel: PvPViewModel
	private lateinit var bracketViewPagerAdapter: BracketViewPagerAdapter

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentPvpBinding.inflate(inflater, container, false)
		viewModel = ViewModelProvider(this).get(PvPViewModel::class.java)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		(requireActivity() as AppCompatActivity).supportActionBar?.show()

		setupUI()
		setupButtons()
		setupObservers()
		setupStrings()
	}

	override fun setupUI() {
		binding.pvpBracketViewPager.offscreenPageLimit = 2
	}

	override fun setupButtons() {
		binding.btnRealmRegionFilter.setOnClickListener {
			viewModel.onFilterClick(0)
			recallFilters()
		}

		binding.btnPvpRegionFilter.setOnClickListener {
			viewModel.onFilterClick(1)
			recallFilters()
		}

		binding.btnSeasonFilter.setOnClickListener {
			viewModel.onFilterClick(2)
			recallFilters()
		}

		binding.btnUpdateLeaderboards.setOnClickListener {
			viewModel.getBrackets()
		}
	}

	override fun setupObservers() {
		viewModel.pvpViewState.observe(viewLifecycleOwner) {
			render(it)
			binding.fragmentPvpLayout.hideKeyboard()
		}

		binding.realmRegionFilterLayout.viewModel.applyClick.observe(viewLifecycleOwner) {
			viewModel.onRealmRegionApplyClick(it)
		}

		binding.realmRegionFilterLayout.viewModel.cancelClick.observe(viewLifecycleOwner) {
			viewModel.onFilterClickClose()
		}

		binding.pvpRegionFilterLayout.viewModel.applyClick.observe(viewLifecycleOwner) {
			viewModel.onPvPRegionApplyClick(it)
		}

		binding.pvpRegionFilterLayout.viewModel.cancelClick.observe(viewLifecycleOwner) {
			viewModel.onFilterClickClose()
		}

		binding.seasonFilterLayout.viewModel.applyClick.observe(viewLifecycleOwner) {
			viewModel.onSeasonApplyClick(it)
		}

		binding.seasonFilterLayout.viewModel.cancelClick.observe(viewLifecycleOwner) {
			viewModel.onFilterClickClose()
		}

		viewModel.brackets.observe(viewLifecycleOwner) {
			bracketViewPagerAdapter =
				BracketViewPagerAdapter(it.first, it.second, childFragmentManager)
			binding.pvpBracketViewPager.adapter = bracketViewPagerAdapter
		}

		viewModel.loadPreviousLeaderboard.observe(viewLifecycleOwner) {
			binding.btnUpdateLeaderboards.callOnClick()
		}
	}

	override fun setupStrings() {

	}

	private fun render(viewState: PvPViewModel.PvPViewState) {
		binding.realmRegionFilterLayout.visibility = viewState.realmRegion
		binding.btnRealmRegionFilter.isSelected = viewState.realmRegion == VISIBLE

		binding.pvpRegionFilterLayout.visibility = viewState.pvpRegion
		binding.btnPvpRegionFilter.isSelected = viewState.pvpRegion == VISIBLE

		binding.seasonFilterLayout.visibility = viewState.season
		binding.btnSeasonFilter.isSelected = viewState.season == VISIBLE

		binding.btnUpdateLeaderboards.visibility = viewState.btnUpdate

		if (viewState.loading == VISIBLE) {
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

	private fun recallFilters() {
		binding.realmRegionFilterLayout.viewModel.recallRealmRegion()
		binding.pvpRegionFilterLayout.viewModel.recallPvPRegion()
		binding.seasonFilterLayout.viewModel.recallSeason()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}