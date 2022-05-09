package pt.mferreira.classicauctions.presentation.auctions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import pt.mferreira.classicauctions.ClassicAuctions
import pt.mferreira.classicauctions.R
import pt.mferreira.classicauctions.databinding.FragmentAuctionsBinding
import pt.mferreira.classicauctions.presentation.common.BaseFragment
import pt.mferreira.classicauctions.utils.extensions.empty
import pt.mferreira.classicauctions.utils.extensions.hideKeyboard

class AuctionsFragment : BaseFragment() {
	private var _binding: FragmentAuctionsBinding? = null
	private val binding get() = _binding!!

	private lateinit var viewModel: AuctionsViewModel

	private lateinit var auctionsRecyclerView: RecyclerView
	private lateinit var auctionsAdapter: AuctionsAdapter

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentAuctionsBinding.inflate(inflater, container, false)
		viewModel = ViewModelProvider(this).get(AuctionsViewModel::class.java)
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
		auctionsRecyclerView = binding.listAuctions
		auctionsRecyclerView.layoutManager = LinearLayoutManager(context)
	}

	override fun setupButtons() {
		binding.btnVersionFilter.setOnClickListener {
			viewModel.onFilterClick(0)
			recallFilters()
		}

		binding.btnRealmFilter.setOnClickListener {
			viewModel.onFilterClick(1)
			recallFilters()
		}

		binding.btnFactionFilter.setOnClickListener {
			viewModel.onFilterClick(2)
			recallFilters()
		}

		binding.btnKeywordFilter.setOnClickListener {
			viewModel.onFilterClick(3)
			recallFilters()
		}

		binding.btnCategoryFilter.setOnClickListener {
			viewModel.onFilterClick(4)
			recallFilters()
		}

		binding.btnSearchAuctions.setOnClickListener {
			viewModel.onSearchClick()
		}

		binding.fabAuctions.setOnClickListener {
			auctionsRecyclerView.smoothScrollToPosition(0)
		}
	}

	override fun setupObservers() {
		viewModel.auctionsViewState.observe(viewLifecycleOwner) {
			render(it)
			binding.fragmentAuctionsLayout.hideKeyboard()
		}

		binding.versionFilterLayout.viewModel.applyClick.observe(viewLifecycleOwner) {
			viewModel.onVersionApplyClick(it)
		}

		binding.versionFilterLayout.viewModel.cancelClick.observe(viewLifecycleOwner) {
			viewModel.onFilterClickClose()
		}

		binding.realmFilterLayout.viewModel.applyClick.observe(viewLifecycleOwner) {
			viewModel.onRealmApplyClick(it)
		}

		binding.realmFilterLayout.viewModel.cancelClick.observe(viewLifecycleOwner) {
			viewModel.onFilterClickClose()
		}

		binding.factionFilterLayout.viewModel.applyClick.observe(viewLifecycleOwner) {
			viewModel.onFactionApplyClick(it)
		}

		binding.factionFilterLayout.viewModel.cancelClick.observe(viewLifecycleOwner) {
			viewModel.onFilterClickClose()
		}

		binding.keywordFilterLayout.viewModel.applyClick.observe(viewLifecycleOwner) {
			viewModel.onKeywordApplyClick(it)
		}

		binding.keywordFilterLayout.viewModel.cancelClick.observe(viewLifecycleOwner) {
			viewModel.onFilterClickClose()
		}

		binding.categoryFilterLayout.viewModel.applyClick.observe(viewLifecycleOwner) {
			viewModel.onCategoryApplyClick(it)
		}

		binding.categoryFilterLayout.viewModel.cancelClick.observe(viewLifecycleOwner) {
			viewModel.onFilterClickClose()
		}

		viewModel.itemSearchResults.observe(viewLifecycleOwner) {
			if (it.fourth) {
				Snackbar.make(
					requireActivity().findViewById(R.id.drawer_layout),
					ClassicAuctions.applicationContext().getString(
						R.string.x_auctions_found,
						it.first.size,
					),
					Snackbar.LENGTH_SHORT
				).show()
			}

			auctionsAdapter = AuctionsAdapter(it.first, it.second, it.third)
			auctionsRecyclerView.adapter = auctionsAdapter

			if (it.first.isEmpty()) {
				binding.labelAuctionsInfo.text = getString(R.string.no_results)
				viewModel.showFab(false)
			} else {
				binding.labelAuctionsInfo.text = String.empty()
				viewModel.showFab(true)
			}
		}
	}

	override fun setupStrings() {

	}

	private fun render(viewState: AuctionsViewModel.AuctionsViewState) {
		binding.versionFilterLayout.visibility = viewState.version
		binding.btnVersionFilter.isSelected = viewState.version == VISIBLE

		binding.realmFilterLayout.visibility = viewState.realm
		binding.btnRealmFilter.isSelected = viewState.realm == VISIBLE

		binding.factionFilterLayout.visibility = viewState.faction
		binding.btnFactionFilter.isSelected = viewState.faction == VISIBLE

		binding.keywordFilterLayout.visibility = viewState.keyword
		binding.btnKeywordFilter.isSelected = viewState.keyword == VISIBLE

		binding.categoryFilterLayout.visibility = viewState.category
		binding.btnCategoryFilter.isSelected = viewState.category == VISIBLE

		binding.btnSearchAuctions.visibility = viewState.search

		binding.fabAuctions.visibility = viewState.fab

		binding.lblLoadingFromCache.visibility = viewState.loadingFromCache
		binding.lblLoadingFromServer.visibility = viewState.loadingFromServer

		if (viewState.loadingFromCache == VISIBLE || viewState.loadingFromServer == VISIBLE) {
			binding.auctionsLoading.visibility = VISIBLE

			activity?.window?.setFlags(
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
			)
		} else {
			binding.auctionsLoading.visibility = GONE
			activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	private fun recallFilters() {
		binding.versionFilterLayout.viewModel.recallVersion()
		binding.realmFilterLayout.viewModel.recallRealm()
		binding.factionFilterLayout.viewModel.recallFaction()
		binding.keywordFilterLayout.viewModel.recallKeyword()
		binding.categoryFilterLayout.viewModel.recallSelectedCategories()
	}
}