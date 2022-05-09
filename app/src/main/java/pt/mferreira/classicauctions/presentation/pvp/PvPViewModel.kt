package pt.mferreira.classicauctions.presentation.pvp

import android.app.Application
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import pt.mferreira.classicauctions.data.entities.PvPEntry
import pt.mferreira.classicauctions.data.network.NetworkClient
import pt.mferreira.classicauctions.data.repositories.LocalPreferencesRepositoryImpl
import pt.mferreira.classicauctions.data.repositories.PvPDatabaseRepositoryImpl
import pt.mferreira.classicauctions.data.repositories.PvPHttpRepositoryImpl
import pt.mferreira.classicauctions.domain.entities.BracketsWrapper
import pt.mferreira.classicauctions.domain.entities.BracketsWrapper.Bracket
import pt.mferreira.classicauctions.domain.usecases.LocalPreferencesUseCase
import pt.mferreira.classicauctions.domain.usecases.PvPDatabaseUseCase
import pt.mferreira.classicauctions.domain.usecases.PvPHttpUseCase
import pt.mferreira.classicauctions.utils.FileUtils
import pt.mferreira.classicauctions.utils.extensions.eu
import pt.mferreira.classicauctions.utils.extensions.us
import java.util.*

class PvPViewModel(application: Application) : AndroidViewModel(application) {
	private var _pvpViewState = PvPViewState()
	val pvpViewState = MutableLiveData<PvPViewState>()

	private val localPreferencesUseCase = LocalPreferencesUseCase(LocalPreferencesRepositoryImpl())

	private val pvpHttpUseCase = PvPHttpUseCase(
		PvPHttpRepositoryImpl.getInstance(NetworkClient.getWoWClient)
	)

	private val pvpDatabaseUseCase =
		PvPDatabaseUseCase(PvPDatabaseRepositoryImpl.getInstance(application))

	private val _brackets: MutableLiveData<Pair<List<Bracket>, Pair<Long, Long>>> =
		MutableLiveData()
	val brackets: LiveData<Pair<List<Bracket>, Pair<Long, Long>>>
		get() = _brackets

	private val _loadPreviousLeaderboard: MutableLiveData<Unit> = MutableLiveData()
	val loadPreviousLeaderboard: LiveData<Unit>
		get() = _loadPreviousLeaderboard

	data class PvPViewState(
		val loading: Int = GONE,
		val bracketsTabLayout: Int = GONE,
		val realmRegion: Int = GONE,
		val pvpRegion: Int = GONE,
		val season: Int = GONE,
		val btnUpdate: Int = GONE,
		val openFilter: Int = -1
	)

	init {
		handleSearchBtnVisibility()

		// Load leaderboards if they already exist.
		viewModelScope.launch {
			try {
				val selectedRealmRegion = localPreferencesUseCase.getSelectedRealmRegion()
				val selectedPvPRegionKey = localPreferencesUseCase.getSelectedPvPRegionKey()
				val selectedSeason = localPreferencesUseCase.getSelectedSeason()
				var currentPvPEntry = PvPEntry()

				if (selectedPvPRegionKey > -1)
					currentPvPEntry =
						pvpDatabaseUseCase.getByData(
							selectedRealmRegion,
							pvpDatabaseUseCase.getByKey(selectedPvPRegionKey).pvpRegionId,
							selectedSeason
						)

				val calendar = Calendar.getInstance()
				calendar.add(Calendar.HOUR, 1)
				val filename = "${currentPvPEntry.region}_" +
						"${currentPvPEntry.pvpRegionId}_" +
						"${currentPvPEntry.seasonId}_" +
						"2v2.json"

				if (FileUtils.doesAnyFileContain(filename))
					_loadPreviousLeaderboard.value = Unit
			} catch (ex: Exception) {
			}
		}
	}

	fun onFilterClick(pos: Int) {
		val bracketsViewState = _pvpViewState.bracketsTabLayout

		if (pos == _pvpViewState.openFilter)
			_pvpViewState = PvPViewState(bracketsTabLayout = bracketsViewState)
		else {
			when (pos) {
				0 -> _pvpViewState = PvPViewState(
					bracketsTabLayout = bracketsViewState,
					realmRegion = VISIBLE,
					openFilter = pos
				)
				1 -> _pvpViewState = PvPViewState(
					bracketsTabLayout = bracketsViewState,
					pvpRegion = VISIBLE,
					openFilter = pos
				)
				2 -> _pvpViewState = PvPViewState(
					bracketsTabLayout = bracketsViewState,
					season = VISIBLE,
					openFilter = pos
				)
			}
		}

		handleSearchBtnVisibility()
	}

	fun onFilterClickClose() {
		_pvpViewState = PvPViewState(bracketsTabLayout = _pvpViewState.bracketsTabLayout)
		handleSearchBtnVisibility()
	}

	fun onRealmRegionApplyClick(selectedRealmRegion: String) {
		when (selectedRealmRegion) {
			String.eu() -> localPreferencesUseCase.setSelectedRealmRegion(String.eu())
			String.us() -> localPreferencesUseCase.setSelectedRealmRegion(String.us())
		}

		localPreferencesUseCase.setSelectedPvPRegionKey(-1)
		localPreferencesUseCase.setSelectedSeason(-1)
	}

	fun onPvPRegionApplyClick(key: Int) {
		localPreferencesUseCase.setSelectedPvPRegionKey(key)
		localPreferencesUseCase.setSelectedSeason(-1)
	}

	fun onSeasonApplyClick(selectedSeason: Int) {
		localPreferencesUseCase.setSelectedSeason(selectedSeason)
	}

	private fun handleSearchBtnVisibility() {
		val selectedRealmRegion = localPreferencesUseCase.getSelectedRealmRegion()
		val selectedPvPRegionKey = localPreferencesUseCase.getSelectedPvPRegionKey()
		val selectedSeason = localPreferencesUseCase.getSelectedSeason()

		_pvpViewState =
			if (selectedRealmRegion.isNotEmpty() && selectedPvPRegionKey > -1 && selectedSeason > -1)
				_pvpViewState.copy(btnUpdate = VISIBLE)
			else
				_pvpViewState.copy(btnUpdate = GONE)

		pvpViewState.value = _pvpViewState
	}

	fun getBrackets() {
		viewModelScope.launch {
			try {
				_pvpViewState = _pvpViewState.copy(loading = VISIBLE)
				pvpViewState.value = _pvpViewState

				val selectedRealmRegion = localPreferencesUseCase.getSelectedRealmRegion()
				val selectedPvPRegionKey = localPreferencesUseCase.getSelectedPvPRegionKey()
				val selectedSeason = localPreferencesUseCase.getSelectedSeason()
				var currentPvPEntry = PvPEntry()

				var pvpRegionId = 0

				if (selectedPvPRegionKey > -1) {
					pvpRegionId = pvpDatabaseUseCase.getByKey(selectedPvPRegionKey).pvpRegionId
					currentPvPEntry =
						pvpDatabaseUseCase.getByData(
							selectedRealmRegion,
							pvpRegionId,
							selectedSeason
						)
				}

				val brackets =
					Gson().fromJson(
						currentPvPEntry.bracketsJson,
						BracketsWrapper::class.java
					).brackets

				var seasonStart = 0L
				var seasonEnd = 0L

				pvpHttpUseCase.getPvPRegionSeasonDetails(
					pvpRegionId,
					selectedSeason,
					selectedRealmRegion
				).either(
					success = {
						it.let {
							seasonStart = it.seasonStart
							seasonEnd = it.seasonEnd ?: 0L
						}
					},
					failure = {}
				)

				_pvpViewState = _pvpViewState.copy(loading = GONE)
				pvpViewState.value = _pvpViewState

				_brackets.value = Pair(brackets, Pair(seasonStart, seasonEnd))
			} catch (ex: Exception) {
			}
		}
	}
}