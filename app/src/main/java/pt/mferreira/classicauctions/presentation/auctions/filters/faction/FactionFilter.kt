package pt.mferreira.classicauctions.presentation.auctions.filters.faction

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import kotlinx.android.synthetic.main.filter_faction.view.*
import pt.mferreira.classicauctions.R

class FactionFilter(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {
	private val view: View
	val viewModel by lazy { ViewModelProvider(context as ViewModelStoreOwner).get(FactionViewModel::class.java) }

	init {
		val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
		view = inflater.inflate(R.layout.filter_faction, this, true)

		setupUI()
		setupButtons()
		setupObservers()
	}

	private fun setupUI() {

	}

	private fun setupButtons() {
		setFactionButtonOnClick(btn_faction_filter_alliance, 0)
		setFactionButtonOnClick(btn_faction_filter_horde, 1)
		setFactionButtonOnClick(btn_faction_filter_neutral, 2)

		btn_faction_filter_apply.setOnClickListener {
			viewModel.onApplyClick()
		}

		btn_faction_filter_close.setOnClickListener {
			viewModel.onClickClose()
		}
	}

	private fun setupObservers() {
		viewModel.factionViewState.observe(context as LifecycleOwner, {
			render(it)
		})
	}

	private fun render(viewState: FactionViewModel.FactionViewState) {
		btn_faction_filter_alliance.isChecked = viewState.isAllianceChecked
		btn_faction_filter_horde.isChecked = viewState.isHordeChecked
		btn_faction_filter_neutral.isChecked = viewState.isNeutralChecked
	}

	private fun setFactionButtonOnClick(button: RadioButton, index: Int) {
		button.setOnClickListener {
			viewModel.onFactionButtonClick(index)
		}
	}
}