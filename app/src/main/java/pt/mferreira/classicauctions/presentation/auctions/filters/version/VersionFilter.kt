package pt.mferreira.classicauctions.presentation.auctions.filters.version

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import kotlinx.android.synthetic.main.filter_version.view.*
import pt.mferreira.classicauctions.R

class VersionFilter(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {
	private val view: View
	val viewModel by lazy { ViewModelProvider(context as ViewModelStoreOwner).get(VersionViewModel::class.java) }

	init {
		val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
		view = inflater.inflate(R.layout.filter_version, this, true)

		setupUI()
		setupButtons()
		setupObservers()
	}

	private fun setupUI() {

	}

	private fun setupButtons() {
		setVersionButtonOnClick(btn_version_filter_era, 0)
		setVersionButtonOnClick(btn_version_filter_progression, 1)

		btn_version_filter_apply.setOnClickListener {
			viewModel.onApplyClick()
		}

		btn_version_filter_close.setOnClickListener {
			viewModel.onClickClose()
		}
	}

	private fun setupObservers() {
		viewModel.versionViewState.observe(context as LifecycleOwner, {
			render(it)
		})
	}

	private fun render(viewState: VersionViewModel.VersionViewState) {
		btn_version_filter_era.isChecked = viewState.isEraChecked
		btn_version_filter_progression.isChecked = viewState.isProgressionChecked
	}

	private fun setVersionButtonOnClick(button: RadioButton, index: Int) {
		button.setOnClickListener {
			viewModel.onVersionButtonClick(index)
		}
	}
}