package pt.mferreira.classicauctions.presentation.auctions.filters.faction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.mferreira.classicauctions.data.repositories.LocalPreferencesRepositoryImpl
import pt.mferreira.classicauctions.domain.usecases.LocalPreferencesUseCase

class FactionViewModel : ViewModel() {
	private var currentFaction = -1

	private lateinit var _factionViewState: FactionViewState
	val factionViewState = MutableLiveData<FactionViewState>()

	data class FactionViewState(
		val isAllianceChecked: Boolean = false,
		val isHordeChecked: Boolean = false,
		val isNeutralChecked: Boolean = false
	)

	private val _applyClick: MutableLiveData<Int> = MutableLiveData()
	val applyClick: LiveData<Int>
		get() = _applyClick

	private val _cancelClick: MutableLiveData<Unit> = MutableLiveData()
	val cancelClick: LiveData<Unit>
		get() = _cancelClick

	init {
		recallFaction()
	}

	fun onFactionButtonClick(index: Int) {
		_factionViewState = when (index) {
			0 -> FactionViewState(isAllianceChecked = true)
			1 -> FactionViewState(isHordeChecked = true)
			else -> FactionViewState(isNeutralChecked = true)
		}

		currentFaction = index
		factionViewState.value = _factionViewState
	}

	fun onApplyClick() {
		if (currentFaction > -1) {
			_applyClick.value = currentFaction
			onClickClose()
		}
	}

	fun onClickClose() {
		_cancelClick.value = Unit
	}

	fun recallFaction() {
		viewModelScope.launch {
			currentFaction =
				LocalPreferencesUseCase(LocalPreferencesRepositoryImpl()).getSelectedFaction()

			_factionViewState = when (currentFaction) {
				0 -> FactionViewState(isAllianceChecked = true)
				1 -> FactionViewState(isHordeChecked = true)
				2 -> FactionViewState(isNeutralChecked = true)
				else -> FactionViewState()
			}

			factionViewState.postValue(_factionViewState)
		}
	}
}