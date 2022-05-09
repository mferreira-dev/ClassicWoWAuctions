package pt.mferreira.classicauctions.presentation.auctions.filters.category

import android.app.Application
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import pt.mferreira.classicauctions.data.entities.ItemCategory
import pt.mferreira.classicauctions.data.entities.ItemSubCategory
import pt.mferreira.classicauctions.data.repositories.ItemDatabaseRepositoryImpl
import pt.mferreira.classicauctions.data.repositories.LocalPreferencesRepositoryImpl
import pt.mferreira.classicauctions.domain.entities.SelectedCategoriesWrapper
import pt.mferreira.classicauctions.domain.usecases.ItemDatabaseUseCase
import pt.mferreira.classicauctions.domain.usecases.LocalPreferencesUseCase

class CategoryViewModel(application: Application) : AndroidViewModel(application) {
	private val itemDatabaseHttpUseCase =
		ItemDatabaseUseCase(ItemDatabaseRepositoryImpl.getInstance(application))

	private val localPreferencesUseCase = LocalPreferencesUseCase(
		LocalPreferencesRepositoryImpl.getInstance()
	)

	private val _categories: MutableLiveData<Pair<List<ItemCategory>, List<ItemSubCategory>>> =
		MutableLiveData()
	val categories: LiveData<Pair<List<ItemCategory>, List<ItemSubCategory>>>
		get() = _categories

	private var selectedCategoriesWrapperBuffer = SelectedCategoriesWrapper(mutableListOf())
	private val _selectedCategoriesWrapper: MutableLiveData<SelectedCategoriesWrapper> =
		MutableLiveData()
	val selectedCategoriesWrapper: LiveData<SelectedCategoriesWrapper>
		get() = _selectedCategoriesWrapper

	private val _updateThroughSubCategory: MutableLiveData<Unit> = MutableLiveData()
	val updateThroughSubCategory: LiveData<Unit>
		get() = _updateThroughSubCategory

	private val _standardUpdate: MutableLiveData<SelectedCategoriesWrapper> = MutableLiveData()
	val standardUpdate: LiveData<SelectedCategoriesWrapper>
		get() = _standardUpdate

	private val _applyClick: MutableLiveData<SelectedCategoriesWrapper> = MutableLiveData()
	val applyClick: LiveData<SelectedCategoriesWrapper>
		get() = _applyClick

	private val _cancelClick: MutableLiveData<Unit> = MutableLiveData()
	val cancelClick: LiveData<Unit>
		get() = _cancelClick

	private lateinit var _categoryViewState: CategoryViewState
	val categoryViewState = MutableLiveData<CategoryViewState>()

	data class CategoryViewState(
		val clear: Int = GONE
	)

	private val gson = Gson()

	init {
		viewModelScope.launch {
			_categories.value = Pair(
				itemDatabaseHttpUseCase.getAllItemCategories(),
				itemDatabaseHttpUseCase.getAllItemSubCategories()
			)
		}
	}

	fun onApplyClick() {
		_applyClick.value = selectedCategoriesWrapperBuffer
		onClickClose()
	}

	fun onClickClose() {
		_cancelClick.value = Unit
	}

	fun onClearClick() {
		_categoryViewState = CategoryViewState()
		categoryViewState.value = _categoryViewState

		selectedCategoriesWrapperBuffer.selectedCategories.forEach {
			it.subCategoryIds.clear()
		}

		_standardUpdate.value = selectedCategoriesWrapperBuffer
	}

	fun handleCategoryClick(categoryId: Int, subCategoryIds: List<Int>) {
		val currentSubCategories =
			selectedCategoriesWrapperBuffer.selectedCategories.find { it.categoryId == categoryId }
		currentSubCategories?.let {
			it.subCategoryIds.clear()
			it.subCategoryIds.addAll(subCategoryIds)
		}

		_standardUpdate.value = selectedCategoriesWrapperBuffer
		hideClearButtonWhenEverythingIsEmpty()
	}

	fun handleSubCategoryClick(categoryId: Int, subCategoryId: Int, isChecked: Boolean) {
		val currentSubCategoriesList =
			selectedCategoriesWrapperBuffer.selectedCategories.find { it.categoryId == categoryId }?.subCategoryIds
		val newSubCategoriesList = mutableListOf<Int>()

		if (isChecked)
			newSubCategoriesList.add(subCategoryId)

		currentSubCategoriesList?.let {
			it.forEach {
				if (it != subCategoryId)
					newSubCategoriesList.add(it)
			}

			currentSubCategoriesList.clear()
			currentSubCategoriesList.addAll(newSubCategoriesList)
		}

		/*
		* If all sub-category check boxes have been checked, the category check box should also be checked.
		* */
		_updateThroughSubCategory.value = Unit
		hideClearButtonWhenEverythingIsEmpty()
	}

	fun recallSelectedCategories() {
		val selectedCategoriesWrapper = gson.fromJson(
			localPreferencesUseCase.getSelectedCategoriesJson(),
			SelectedCategoriesWrapper::class.java
		)

		selectedCategoriesWrapperBuffer =
			selectedCategoriesWrapper ?: SelectedCategoriesWrapper(mutableListOf())
		_selectedCategoriesWrapper.value = selectedCategoriesWrapperBuffer
		_updateThroughSubCategory.value = Unit

		hideClearButtonWhenEverythingIsEmpty()
	}

	private fun hideClearButtonWhenEverythingIsEmpty() {
		var noSubCategoriesSelected = true
		selectedCategoriesWrapperBuffer.selectedCategories.forEach {
			if (it.subCategoryIds.isNotEmpty())
				noSubCategoriesSelected = false
		}

		_categoryViewState = if (!noSubCategoriesSelected)
			CategoryViewState(VISIBLE)
		else
			CategoryViewState()

		categoryViewState.value = _categoryViewState
	}
}