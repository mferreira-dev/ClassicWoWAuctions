package pt.mferreira.classicauctions.presentation.pvp.filters.pvp_region

import android.app.Application
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.mferreira.classicauctions.data.entities.PvPEntry
import pt.mferreira.classicauctions.data.repositories.LocalPreferencesRepositoryImpl
import pt.mferreira.classicauctions.data.repositories.PvPDatabaseRepositoryImpl
import pt.mferreira.classicauctions.domain.usecases.LocalPreferencesUseCase
import pt.mferreira.classicauctions.domain.usecases.PvPDatabaseUseCase
import pt.mferreira.classicauctions.utils.extensions.empty

class PvPRegionViewModel(application: Application) : AndroidViewModel(application) {
	var currentPvPRegionKey = -1
	var currentPvPEntry = PvPEntry()

	private var _pvpRegionViewState = PvPRegionViewState()
	val pvpRegionViewState = MutableLiveData<PvPRegionViewState>()

	private val _currentPvPRegion: MutableLiveData<Pair<List<PvPEntry>, PvPEntry>> =
		MutableLiveData()
	val currentPvPRegion: LiveData<Pair<List<PvPEntry>, PvPEntry>>
		get() = _currentPvPRegion

	private val pvpDatabaseUseCase =
		PvPDatabaseUseCase(PvPDatabaseRepositoryImpl.getInstance(application))

	private val localPreferencesUseCase = LocalPreferencesUseCase(
		LocalPreferencesRepositoryImpl.getInstance()
	)

	private val allPvPEntries = mutableListOf<PvPEntry>()

	private val _applyClick: MutableLiveData<Int> = MutableLiveData()
	val applyClick: LiveData<Int>
		get() = _applyClick

	private val _cancelClick: MutableLiveData<Unit> = MutableLiveData()
	val cancelClick: LiveData<Unit>
		get() = _cancelClick

	data class PvPRegionViewState(
		val selectRealmRegionFirst: Int = GONE,
		val btnApply: Int = GONE
	)

	init {
		viewModelScope.launch {
			allPvPEntries.addAll(pvpDatabaseUseCase.getAll())
		}

		recallPvPRegion()
	}

	fun onApplyClick(pvpRegionId: Int) {
		_applyClick.value = pvpRegionId
		onClickClose()
	}

	fun onClickClose() {
		_cancelClick.value = Unit
	}

	fun recallPvPRegion() {
		viewModelScope.launch {
			currentPvPRegionKey = localPreferencesUseCase.getSelectedPvPRegionKey()

			if (currentPvPRegionKey > -1)
				currentPvPEntry = pvpDatabaseUseCase.getByKey(currentPvPRegionKey)

			val selectedRealmRegion = localPreferencesUseCase.getSelectedRealmRegion()
			_pvpRegionViewState = if (selectedRealmRegion == String.empty())
				PvPRegionViewState(selectRealmRegionFirst = VISIBLE)
			else
				PvPRegionViewState(btnApply = VISIBLE)

			val filteredPvPEntries = mutableListOf<PvPEntry>()
			filteredPvPEntries.addAll(
				allPvPEntries
					.filter { it.region == selectedRealmRegion }
					.sortedBy { it.pvpRegionId }
					.distinctBy { it.pvpRegionId }
			)

			pvpRegionViewState.value = _pvpRegionViewState
			_currentPvPRegion.value = Pair(filteredPvPEntries, currentPvPEntry)
		}
	}
}