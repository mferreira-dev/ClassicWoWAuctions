package pt.mferreira.classicauctions.presentation.pvp.filters.season

import android.app.Application
import android.view.View
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

class SeasonViewModel(application: Application) : AndroidViewModel(application) {
	private var selectedSeason = -1

	private var _seasonViewState = SeasonViewState()
	val seasonViewState = MutableLiveData<SeasonViewState>()

	private val _currentlySelectedSeason: MutableLiveData<Pair<Int, List<Int>>> =
		MutableLiveData()
	val currentlySelectedSeason: LiveData<Pair<Int, List<Int>>>
		get() = _currentlySelectedSeason

	private val pvpDatabaseUseCase =
		PvPDatabaseUseCase(PvPDatabaseRepositoryImpl.getInstance(application))

	private val localPreferencesUseCase = LocalPreferencesUseCase(
		LocalPreferencesRepositoryImpl.getInstance()
	)

	private val _applyClick: MutableLiveData<Int> = MutableLiveData()
	val applyClick: LiveData<Int>
		get() = _applyClick

	private val _cancelClick: MutableLiveData<Unit> = MutableLiveData()
	val cancelClick: LiveData<Unit>
		get() = _cancelClick

	data class SeasonViewState(
		val selectOthersFirst: Int = View.GONE,
		val btnApply: Int = View.GONE
	)

	init {
		recallSeason()
	}

	fun onApplyClick(season: Int) {
		_applyClick.value = season
		onClickClose()
	}

	fun onClickClose() {
		_cancelClick.value = Unit
	}

	fun recallSeason() {
		viewModelScope.launch {
			selectedSeason = localPreferencesUseCase.getSelectedSeason()
			val region = localPreferencesUseCase.getSelectedRealmRegion()
			val currentPvPRegionKey = localPreferencesUseCase.getSelectedPvPRegionKey()
			var currentPvPEntry = PvPEntry()

			if (currentPvPRegionKey > -1)
				currentPvPEntry = pvpDatabaseUseCase.getByKey(currentPvPRegionKey)

			val seasons = mutableListOf<Int>()
			seasons.addAll(
				pvpDatabaseUseCase.getSeasonsByRegionId(
					region,
					currentPvPEntry.pvpRegionId
				).distinctBy { it })

			_seasonViewState = if (region.isEmpty() || currentPvPEntry.pvpRegionId == -1)
				SeasonViewState(selectOthersFirst = VISIBLE)
			else
				SeasonViewState(btnApply = VISIBLE)

			seasonViewState.value = _seasonViewState
			_currentlySelectedSeason.value = Pair(selectedSeason, seasons)
		}
	}
}