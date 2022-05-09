package pt.mferreira.classicauctions.presentation.auctions.filters.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.mferreira.classicauctions.R
import pt.mferreira.classicauctions.data.entities.ItemSubCategory
import pt.mferreira.classicauctions.domain.entities.SelectedCategoriesWrapper

class SubCategoryAdapter(
	private val selectedCategoriesWrapper: SelectedCategoriesWrapper,
	private val subCategories: List<ItemSubCategory>,
	private val viewModel: CategoryViewModel
) : RecyclerView.Adapter<SubCategoryAdapter.SubCategoryViewHolder>() {

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): SubCategoryViewHolder {
		return SubCategoryViewHolder(
			LayoutInflater.from(parent.context)
				.inflate(R.layout.row_sub_category, parent, false)
		)
	}

	override fun onBindViewHolder(holder: SubCategoryViewHolder, position: Int) {
		holder.bind(subCategories[position])

		val parentCategoryId = subCategories[holder.adapterPosition].categoryId
		val currentSubCategoryId = subCategories[holder.adapterPosition].id

		selectedCategoriesWrapper.selectedCategories.forEach { category ->
			if (parentCategoryId == category.categoryId) {
				category.subCategoryIds.forEach { subCategoryId ->
					if (currentSubCategoryId == subCategoryId)
						holder.subCategoryCheckBox.isChecked = true
				}
			}
		}

		holder.subCategoryCheckBox.setOnCheckedChangeListener { _, isChecked ->
			viewModel.handleSubCategoryClick(
				parentCategoryId,
				subCategories[position].id,
				isChecked
			)
		}
	}

	override fun getItemCount(): Int {
		return subCategories.size
	}

	inner class SubCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		private val labelSubCategoryName =
			itemView.findViewById(R.id.row_sub_category_name_label) as TextView
		val subCategoryCheckBox = itemView.findViewById(R.id.row_sub_category_box) as CheckBox

		fun bind(subCategory: ItemSubCategory) {
			labelSubCategoryName.text = subCategory.name
		}
	}
}