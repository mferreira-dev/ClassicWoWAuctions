package pt.mferreira.classicauctions.presentation.auctions.filters.version

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.mferreira.classicauctions.data.repositories.LocalPreferencesRepositoryImpl
import pt.mferreira.classicauctions.domain.usecases.LocalPreferencesUseCase

class VersionViewModel : ViewModel() {
	private var currentVersion = -1

	private lateinit var _versionViewState: VersionViewState
	val versionViewState = MutableLiveData<VersionViewState>()

	data class VersionViewState(
		val isEraChecked: Boolean = false,
		val isProgressionChecked: Boolean = false
	)

	private val _applyClick: MutableLiveData<Int> = MutableLiveData()
	val applyClick: LiveData<Int>
		get() = _applyClick

	private val _cancelClick: MutableLiveData<Unit> = MutableLiveData()
	val cancelClick: LiveData<Unit>
		get() = _cancelClick

	init {
		recallVersion()
	}

	fun onVersionButtonClick(index: Int) {
		_versionViewState = if (index == 0)
			VersionViewState(isEraChecked = true)
		else
			VersionViewState(isProgressionChecked = true)

		currentVersion = index
		versionViewState.value = _versionViewState
	}

	fun onApplyClick() {
		if (currentVersion > -1) {
			_applyClick.value = currentVersion
			onClickClose()
		}
	}

	fun onClickClose() {
		_cancelClick.value = Unit
	}

	fun recallVersion() {
		viewModelScope.launch {
			currentVersion = LocalPreferencesUseCase(LocalPreferencesRepositoryImpl())
				.getSelectedVersion()

			_versionViewState = when (currentVersion) {
				0 -> VersionViewState(isEraChecked = true)
				1 -> VersionViewState(isProgressionChecked = true)
				else -> VersionViewState()
			}

			versionViewState.postValue(_versionViewState)
		}
	}
}