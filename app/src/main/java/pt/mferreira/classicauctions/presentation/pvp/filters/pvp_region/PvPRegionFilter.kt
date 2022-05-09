package pt.mferreira.classicauctions.presentation.pvp.filters.pvp_region

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.filter_pvp_region.view.*
import pt.mferreira.classicauctions.R
import pt.mferreira.classicauctions.data.entities.PvPEntry

class PvPRegionFilter(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {
	private val view: View
	val viewModel by lazy { ViewModelProvider(context as ViewModelStoreOwner).get(PvPRegionViewModel::class.java) }

	private lateinit var pvpRegionRecyclerView: RecyclerView
	private lateinit var pvpRegionAdapter: PvPRegionAdapter
	private lateinit var filteredPvPEntries: List<PvPEntry>

	init {
		val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
		view = inflater.inflate(R.layout.filter_pvp_region, this, true)

		setupUI()
		setupButtons()
		setupObservers()
	}

	private fun setupUI() {
		pvpRegionRecyclerView = list_pvp_region_filter
		pvpRegionRecyclerView.layoutManager = LinearLayoutManager(context)
	}

	private fun setupButtons() {
		btn_pvp_region_filter_apply.setOnClickListener {
			viewModel.onApplyClick(filteredPvPEntries[pvpRegionAdapter.selectedIndex].aKey)
		}

		btn_pvp_region_filter_close.setOnClickListener {
			viewModel.onClickClose()
		}
	}

	private fun setupObservers() {
		viewModel.pvpRegionViewState.observe(context as LifecycleOwner) {
			render(it)
		}

		viewModel.currentPvPRegion.observe(context as LifecycleOwner) {
			filteredPvPEntries = it.first
			recallPvPRegion(it.second)
		}
	}

	private fun render(viewState: PvPRegionViewModel.PvPRegionViewState) {
		lbl_pvp_region_filter_select_realm_region_first.visibility =
			viewState.selectRealmRegionFirst

		btn_pvp_region_filter_apply.visibility = viewState.btnApply
	}

	private fun recallPvPRegion(selectedPvPEntry: PvPEntry) {
		pvpRegionAdapter = PvPRegionAdapter(context, filteredPvPEntries)
		pvpRegionRecyclerView.adapter = pvpRegionAdapter

		filteredPvPEntries.forEachIndexed { idx, ele ->
			if (ele.aKey == selectedPvPEntry.aKey) {
				pvpRegionRecyclerView.scrollToPosition(idx)
				pvpRegionAdapter.selectedIndex = idx
			}
		}
	}
}