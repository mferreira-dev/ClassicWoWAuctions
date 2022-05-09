package pt.mferreira.classicauctions.presentation.auctions.filters.item

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import kotlinx.android.synthetic.main.filter_item.view.*
import pt.mferreira.classicauctions.R
import pt.mferreira.classicauctions.utils.extensions.empty
import pt.mferreira.classicauctions.utils.extensions.hideKeyboard

class KeywordFilter(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {
	private val view: View
	val viewModel by lazy { ViewModelProvider(context as ViewModelStoreOwner).get(KeywordViewModel::class.java) }

	init {
		val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
		view = inflater.inflate(R.layout.filter_item, this, true)

		setupButtons()
		setupObservers()
	}

	private fun setupButtons() {
		keyword_filter_input.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
			override fun afterTextChanged(s: Editable?) {}
			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
				if (keyword_filter_input.text.isNotEmpty())
					btn_clear_input.visibility = VISIBLE
				else
					btn_clear_input.visibility = GONE
			}
		})

		keyword_filter_input.setOnEditorActionListener { _, actionId, _ ->
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				if (keyword_filter_input.text.toString() != String.empty())
					btn_keyword_filter_apply.callOnClick()

				true
			} else
				false
		}

		btn_clear_input.setOnClickListener {
			keyword_filter_input.setText(String.empty())
		}

		btn_keyword_filter_apply.setOnClickListener {
			viewModel.onApplyClick(keyword_filter_input.text.toString())
			view.hideKeyboard()
		}

		btn_keyword_filter_close.setOnClickListener {
			viewModel.onClickClose()
			view.hideKeyboard()
		}
	}

	fun setupObservers() {
		viewModel.keyword.observe(context as LifecycleOwner, {
			keyword_filter_input.setText(it)
		})
	}
}