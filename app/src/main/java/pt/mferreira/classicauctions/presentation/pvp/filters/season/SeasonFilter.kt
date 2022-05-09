package pt.mferreira.classicauctions.presentation.pvp.filters.season

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
import kotlinx.android.synthetic.main.filter_season.view.*
import pt.mferreira.classicauctions.R

class SeasonFilter(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {
	private val view: View
	val viewModel by lazy { ViewModelProvider(context as ViewModelStoreOwner).get(SeasonViewModel::class.java) }

	private lateinit var seasonRecyclerView: RecyclerView
	private lateinit var seasonAdapter: SeasonAdapter
	private lateinit var seasons: List<Int>

	init {
		val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
		view = inflater.inflate(R.layout.filter_season, this, true)

		setupUI()
		setupButtons()
		setupObservers()
	}

	private fun setupUI() {
		seasonRecyclerView = list_season_filter
		seasonRecyclerView.layoutManager = LinearLayoutManager(context)
	}

	private fun setupButtons() {
		btn_season_filter_apply.setOnClickListener {
			viewModel.onApplyClick(seasons[seasonAdapter.selectedIndex])
		}

		btn_season_filter_close.setOnClickListener {
			viewModel.onClickClose()
		}
	}

	private fun setupObservers() {
		viewModel.seasonViewState.observe(context as LifecycleOwner, {
			render(it)
		})

		viewModel.currentlySelectedSeason.observe(context as LifecycleOwner, {
			seasons = it.second
			recallSeason(it.first)
		})
	}

	private fun render(viewState: SeasonViewModel.SeasonViewState) {
		lbl_season_filter_select_others_first.visibility =
			viewState.selectOthersFirst

		btn_season_filter_apply.visibility = viewState.btnApply
	}

	private fun recallSeason(season: Int) {
		seasonAdapter = SeasonAdapter(context, seasons)
		seasonRecyclerView.adapter = seasonAdapter

		seasons.forEachIndexed { idx, ele ->
			if (ele == season) {
				seasonRecyclerView.scrollToPosition(idx)
				seasonAdapter.selectedIndex = idx
			}
		}
	}
}