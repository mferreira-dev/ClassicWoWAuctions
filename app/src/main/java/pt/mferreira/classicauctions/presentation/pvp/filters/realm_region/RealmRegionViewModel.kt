package pt.mferreira.classicauctions.presentation.pvp.filters.realm_region

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.mferreira.classicauctions.data.repositories.LocalPreferencesRepositoryImpl
import pt.mferreira.classicauctions.domain.usecases.LocalPreferencesUseCase
import pt.mferreira.classicauctions.utils.extensions.empty
import pt.mferreira.classicauctions.utils.extensions.eu
import pt.mferreira.classicauctions.utils.extensions.us

class RealmRegionViewModel : ViewModel() {
	private var currentRealmRegion = String.empty()

	private lateinit var _realmRegionViewState: RealmRegionViewState
	val realmRegionViewState = MutableLiveData<RealmRegionViewState>()

	data class RealmRegionViewState(
		val isEuChecked: Boolean = false,
		val isUsChecked: Boolean = false
	)

	private val _applyClick: MutableLiveData<String> = MutableLiveData()
	val applyClick: LiveData<String>
		get() = _applyClick

	private val _cancelClick: MutableLiveData<Unit> = MutableLiveData()
	val cancelClick: LiveData<Unit>
		get() = _cancelClick

	init {
		recallRealmRegion()
	}

	fun onRealmRegionButtonClick(selectedRealmRegion: String) {
		_realmRegionViewState = when (selectedRealmRegion) {
			String.eu() -> RealmRegionViewState(isEuChecked = true)
			String.us() -> RealmRegionViewState(isUsChecked = true)
			else -> RealmRegionViewState()
		}

		currentRealmRegion = selectedRealmRegion
		realmRegionViewState.value = _realmRegionViewState
	}

	fun onApplyClick() {
		if (currentRealmRegion.isNotEmpty()) {
			_applyClick.value = currentRealmRegion
			onClickClose()
		}
	}

	fun onClickClose() {
		_cancelClick.value = Unit
	}

	fun recallRealmRegion() {
		viewModelScope.launch {
			currentRealmRegion =
				LocalPreferencesUseCase(LocalPreferencesRepositoryImpl()).getSelectedRealmRegion()

			_realmRegionViewState = when (currentRealmRegion) {
				String.eu() -> RealmRegionViewState(isEuChecked = true)
				String.us() -> RealmRegionViewState(isUsChecked = true)
				else -> RealmRegionViewState()
			}

			realmRegionViewState.postValue(_realmRegionViewState)
		}
	}
}