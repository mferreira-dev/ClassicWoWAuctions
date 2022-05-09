package pt.mferreira.classicauctions.presentation.auctions.filters.category

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
import kotlinx.android.synthetic.main.filter_category.view.*
import pt.mferreira.classicauctions.R
import pt.mferreira.classicauctions.data.entities.ItemCategory
import pt.mferreira.classicauctions.data.entities.ItemSubCategory

class CategoryFilter(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {
	private val view: View
	val viewModel by lazy { ViewModelProvider(context as ViewModelStoreOwner).get(CategoryViewModel::class.java) }

	private lateinit var categoryRecyclerView: RecyclerView
	private lateinit var categoryAdapter: CategoryAdapter
	private lateinit var categories: List<ItemCategory>
	private lateinit var subCategories: List<ItemSubCategory>

	init {
		val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
		view = inflater.inflate(R.layout.filter_category, this, true)

		setupUI()
		setupButtons()
		setupObservers()
	}

	private fun setupUI() {
		categoryRecyclerView = findViewById(R.id.list_category_filter)
		categoryRecyclerView.layoutManager = LinearLayoutManager(context)
	}

	private fun setupButtons() {
		btn_category_filter_apply.setOnClickListener {
			viewModel.onApplyClick()
		}

		btn_category_filter_close.setOnClickListener {
			viewModel.onClickClose()
		}

		btn_category_filter_clear.setOnClickListener {
			viewModel.onClearClick()
		}
	}

	private fun setupObservers() {
		viewModel.categories.observe(context as LifecycleOwner, {
			categories = it.first
			subCategories = it.second
			categoryAdapter = CategoryAdapter(context, categories, subCategories, viewModel)
			categoryRecyclerView.adapter = categoryAdapter
		})

		viewModel.selectedCategoriesWrapper.observe(context as LifecycleOwner, {
			categoryAdapter.selectedCategoriesWrapper = it
		})

		viewModel.updateThroughSubCategory.observe(context as LifecycleOwner, {
			categoryAdapter.notifyDataSetChanged()
		})

		viewModel.standardUpdate.observe(context as LifecycleOwner, {
			categoryAdapter = CategoryAdapter(context, categories, subCategories, viewModel)
			categoryRecyclerView.adapter = categoryAdapter
			categoryAdapter.selectedCategoriesWrapper = it
		})

		viewModel.categoryViewState.observe(context as LifecycleOwner, {
			render(it)
		})
	}

	private fun render(viewState: CategoryViewModel.CategoryViewState) {
		btn_category_filter_clear.visibility = viewState.clear
	}
}