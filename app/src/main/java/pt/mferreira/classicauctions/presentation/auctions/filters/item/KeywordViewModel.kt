package pt.mferreira.classicauctions.presentation.auctions.filters.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.mferreira.classicauctions.data.repositories.LocalPreferencesRepositoryImpl
import pt.mferreira.classicauctions.domain.usecases.LocalPreferencesUseCase

class KeywordViewModel : ViewModel() {
	private val _keyword: MutableLiveData<String> = MutableLiveData()
	val keyword: LiveData<String>
		get() = _keyword

	private val _applyClick: MutableLiveData<String> = MutableLiveData()
	val applyClick: LiveData<String>
		get() = _applyClick

	private val _cancelClick: MutableLiveData<Unit> = MutableLiveData()
	val cancelClick: LiveData<Unit>
		get() = _cancelClick

	init {
		recallKeyword()
	}

	fun onApplyClick(keyword: String) {
		if (keyword.isNotEmpty()) {
			_applyClick.value = keyword
			onClickClose()
		}
	}

	fun onClickClose() {
		_cancelClick.value = Unit
	}

	fun recallKeyword() {
		viewModelScope.launch {
			_keyword.postValue(LocalPreferencesUseCase(LocalPreferencesRepositoryImpl()).getItemKeyword())
		}
	}
}