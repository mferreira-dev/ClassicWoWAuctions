package pt.mferreira.classicauctions.presentation.auctions.filters.category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.mferreira.classicauctions.R
import pt.mferreira.classicauctions.data.entities.ItemCategory
import pt.mferreira.classicauctions.data.entities.ItemSubCategory
import pt.mferreira.classicauctions.domain.entities.SelectedCategoriesWrapper

class CategoryAdapter(
	private val context: Context,
	private val allCategories: List<ItemCategory>,
	private val allSubCategories: List<ItemSubCategory>,
	private val viewModel: CategoryViewModel
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

	var selectedCategoriesWrapper = SelectedCategoriesWrapper(mutableListOf())

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): CategoryViewHolder {
		return CategoryViewHolder(
			LayoutInflater.from(parent.context).inflate(R.layout.row_category, parent, false)
		)
	}

	override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
		holder.bind(allCategories[position])

		/*
		* If a category has sub-categories and all of them have been checked then the main one
		* should also be checked.
		* */
		selectedCategoriesWrapper.selectedCategories.forEach {
			if (it.categoryId == allCategories[holder.adapterPosition].id) {
				val subCategoriesSize =
					allSubCategories.filter { it.categoryId == allCategories[holder.adapterPosition].id }.size
				val selectedSubCategoriesSize = it.subCategoryIds.size

				holder.categoryCheckBox.isChecked =
					selectedSubCategoriesSize > 0 && selectedSubCategoriesSize == subCategoriesSize

				if (selectedSubCategoriesSize > 0) {
					holder.subCategoryRecyclerView.visibility = VISIBLE
					holder.categoryChevron.isSelected = true
				}
			}
		}

		holder.cardView.setOnClickListener {
			val chevron = holder.categoryChevron
			chevron.isSelected = !chevron.isSelected
			holder.subCategoryRecyclerView.visibility = if (chevron.isSelected) VISIBLE else GONE
		}

		// If checked, automatically check all sub-categories.
		holder.categoryCheckBox.setOnCheckedChangeListener { _, isChecked ->
			val subCategoryIds = mutableListOf<Int>()
			if (isChecked) {
				allSubCategories.filter { it.categoryId == allCategories[holder.adapterPosition].id }
					.forEach {
						subCategoryIds.add(it.id)
					}
			}

			viewModel.handleCategoryClick(allCategories[holder.adapterPosition].id, subCategoryIds)
		}
	}

	override fun getItemCount(): Int {
		return allCategories.size
	}

	inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val cardView = itemView.findViewById(R.id.row_category_layout) as CardView
		val categoryCheckBox = itemView.findViewById(R.id.row_category_box) as CheckBox
		private val labelCategoryName =
			itemView.findViewById(R.id.row_category_name_label) as TextView
		val categoryChevron = itemView.findViewById(R.id.row_category_chevron) as ImageView

		val subCategoryRecyclerView =
			itemView.findViewById(R.id.row_sub_category_list) as RecyclerView
		private lateinit var subCategoryAdapter: SubCategoryAdapter

		fun bind(category: ItemCategory) {
			labelCategoryName.text = category.name
			categoryChevron.isSelected = false
			subCategoryRecyclerView.visibility = GONE

			val localSubCategories = allSubCategories.filter { it.categoryId == category.id }
			subCategoryRecyclerView.layoutManager = LinearLayoutManager(context)
			subCategoryAdapter =
				SubCategoryAdapter(selectedCategoriesWrapper, localSubCategories, viewModel)
			subCategoryRecyclerView.adapter = subCategoryAdapter

			if (localSubCategories.isEmpty())
				categoryChevron.visibility = GONE
			else
				categoryChevron.visibility = VISIBLE
		}
	}
}