package pt.mferreira.classicauctions.presentation.pvp.filters.realm_region

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import kotlinx.android.synthetic.main.filter_realm_region.view.*
import pt.mferreira.classicauctions.R
import pt.mferreira.classicauctions.utils.extensions.eu
import pt.mferreira.classicauctions.utils.extensions.us

class RealmRegionFilter(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {
	private val view: View
	val viewModel by lazy {
		ViewModelProvider(context as ViewModelStoreOwner).get(
			RealmRegionViewModel::class.java
		)
	}

	init {
		val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
		view = inflater.inflate(R.layout.filter_realm_region, this, true)

		setupUI()
		setupButtons()
		setupObservers()
	}

	private fun setupUI() {

	}

	private fun setupButtons() {
		setRealmRegionButtonOnClick(btn_realm_region_filter_eu, String.eu())
		setRealmRegionButtonOnClick(btn_realm_region_filter_us, String.us())

		btn_realm_region_filter_apply.setOnClickListener {
			viewModel.onApplyClick()
		}

		btn_realm_region_filter_close.setOnClickListener {
			viewModel.onClickClose()
		}
	}

	private fun setupObservers() {
		viewModel.realmRegionViewState.observe(context as LifecycleOwner, {
			render(it)
		})
	}

	private fun render(viewState: RealmRegionViewModel.RealmRegionViewState) {
		btn_realm_region_filter_eu.isChecked = viewState.isEuChecked
		btn_realm_region_filter_us.isChecked = viewState.isUsChecked
	}

	private fun setRealmRegionButtonOnClick(button: RadioButton, selectedRealmRegion: String) {
		button.setOnClickListener {
			viewModel.onRealmRegionButtonClick(selectedRealmRegion)
		}
	}
}