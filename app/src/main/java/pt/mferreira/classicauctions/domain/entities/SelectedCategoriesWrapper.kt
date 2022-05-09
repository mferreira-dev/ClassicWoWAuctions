package pt.mferreira.classicauctions.domain.entities

/**
 * Used as a wrapper for database operations (Room only accepts primitive types, hence the need for a JSON string).
 * */
data class SelectedCategoriesWrapper(
	val selectedCategories: MutableList<SelectedCategory>
) {
	data class SelectedCategory(
		val categoryId: Int,
		val subCategoryIds: MutableList<Int>
	)
}