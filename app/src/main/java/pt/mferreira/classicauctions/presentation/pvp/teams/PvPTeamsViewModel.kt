package pt.mferreira.classicauctions.presentation.pvp.teams

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
import pt.mferreira.classicauctions.data.models.LeaderboardDetailsResponse.Entry
import pt.mferreira.classicauctions.data.network.NetworkClient
import pt.mferreira.classicauctions.data.repositories.LocalPreferencesRepositoryImpl
import pt.mferreira.classicauctions.data.repositories.PvPDatabaseRepositoryImpl
import pt.mferreira.classicauctions.data.repositories.PvPHttpRepositoryImpl
import pt.mferreira.classicauctions.domain.entities.BracketsWrapper.Bracket
import pt.mferreira.classicauctions.domain.usecases.LocalPreferencesUseCase
import pt.mferreira.classicauctions.domain.usecases.PvPDatabaseUseCase
import pt.mferreira.classicauctions.domain.usecases.PvPHttpUseCase
import pt.mferreira.classicauctions.utils.FileUtils
import java.io.File
import java.util.*

class PvPTeamsViewModel(application: Application) : AndroidViewModel(application) {
	private val app = application

	private var _pvpTeamsViewState = PvPTeamsViewState()
	val pvpTeamsViewState = MutableLiveData<PvPTeamsViewState>()

	private val _leaderboard: MutableLiveData<List<Entry>> = MutableLiveData()
	val leaderboard: LiveData<List<Entry>>
		get() = _leaderboard

	private val localPreferencesUseCase =
		LocalPreferencesUseCase(LocalPreferencesRepositoryImpl.getInstance())

	private val pvpDatabaseUseCase = PvPDatabaseUseCase(
		PvPDatabaseRepositoryImpl.getInstance(app)
	)

	private val pvpHttpUseCase = PvPHttpUseCase(
		PvPHttpRepositoryImpl.getInstance(NetworkClient.getWoWClient)
	)

	data class PvPTeamsViewState(
		val loadingFromCache: Int = GONE,
		val loadingFromServer: Int = GONE
	)

	/**
	 * Updates the first bracket (2v2) file if it has expired or does not exist yet and returns it.
	 * @return A file with up-to-date leaderboard rankings.
	 * */
	fun updateLeaderboard(bracket: Bracket) {
		viewModelScope.launch {
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

			var shouldUpdateLeaderboard = true
			val files = FileUtils.listFiles()

			// Prepare filename (will be replaced if auctions haven't expired yet).
			val calendar = Calendar.getInstance()
			calendar.add(Calendar.HOUR, 1)
			var filename = "${calendar.timeInMillis}_" +
					"${currentPvPEntry.region}_" +
					"${currentPvPEntry.pvpRegionId}_" +
					"${currentPvPEntry.seasonId}_" +
					"${bracket.name}.json"

			files?.forEach {
				if (it.name.contains(filename.substring(filename.indexOf("_") + 1))) {
					// Match found, check if it has expired.
					val expiration = it.name.substring(0, it.name.indexOf("_")).toLong()

					shouldUpdateLeaderboard = System.currentTimeMillis() >= expiration

					if (!shouldUpdateLeaderboard)
						filename = it.name

					return@forEach
				}
			}

			// True if the file does not exist or has expired.
			if (shouldUpdateLeaderboard) {
				_pvpTeamsViewState = PvPTeamsViewState(loadingFromServer = VISIBLE)
				pvpTeamsViewState.value = _pvpTeamsViewState

				getLeaderboard(
					currentPvPEntry.pvpRegionId,
					currentPvPEntry.seasonId,
					bracket.name,
					currentPvPEntry.region
				)
			} else {
				_pvpTeamsViewState = PvPTeamsViewState(loadingFromCache = VISIBLE)
				pvpTeamsViewState.value = _pvpTeamsViewState
			}

			getLeaderboard(bracket.name)

			_pvpTeamsViewState = PvPTeamsViewState()
			pvpTeamsViewState.value = _pvpTeamsViewState
		}
	}

	private suspend fun getLeaderboard(
		pvpRegionId: Int,
		seasonId: Int,
		bracket: String,
		region: String
	) {
		pvpHttpUseCase.getLeaderboardDetails(pvpRegionId, seasonId, bracket, region)
			.either(
				success = { resp ->
					val calendar = Calendar.getInstance()
					calendar.add(Calendar.HOUR, 1)

					val filename = "" +
							"${calendar.timeInMillis}_" +
							"${region}_" +
							"${pvpRegionId}_" +
							"${seasonId}_" +
							"${bracket}.json"

					FileUtils.write(File(app.filesDir, filename), resp.entries)
				},
				failure = {}
			)
	}

	private fun getLeaderboard(bracket: String) {
		viewModelScope.launch {
			val realmRegion = localPreferencesUseCase.getSelectedRealmRegion()
			val pvpRegion =
				pvpDatabaseUseCase.getByKey(localPreferencesUseCase.getSelectedPvPRegionKey()).pvpRegionId
			val season = localPreferencesUseCase.getSelectedSeason()
			val filename = "${realmRegion}_${pvpRegion}_${season}_${bracket}.json"

			val leaderboardEntries: Array<Entry>
			val reader = FileUtils.findAndRead(filename)

			reader.let {
				leaderboardEntries = Gson().fromJson(reader, Array<Entry>::class.java)
				_leaderboard.value = leaderboardEntries.asList()
			}
		}
	}
}