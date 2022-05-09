package pt.mferreira.classicauctions.presentation.auctions

import android.app.Application
import android.content.Context
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import pt.mferreira.classicauctions.ClassicAuctions
import pt.mferreira.classicauctions.R
import pt.mferreira.classicauctions.data.entities.Realm
import pt.mferreira.classicauctions.data.models.Auction
import pt.mferreira.classicauctions.data.models.ItemSearchResponse
import pt.mferreira.classicauctions.data.network.NetworkClient
import pt.mferreira.classicauctions.data.repositories.*
import pt.mferreira.classicauctions.domain.entities.AuctionQuerySettings
import pt.mferreira.classicauctions.domain.entities.AuctionRealmData
import pt.mferreira.classicauctions.domain.entities.Quadruple
import pt.mferreira.classicauctions.domain.entities.SelectedCategoriesWrapper
import pt.mferreira.classicauctions.domain.usecases.*
import pt.mferreira.classicauctions.utils.FileUtils
import pt.mferreira.classicauctions.utils.extensions.empty
import pt.mferreira.classicauctions.utils.extensions.eu
import java.io.File
import java.util.*

// Git test.
class AuctionsViewModel(application: Application) : AndroidViewModel(application) {
	private var app = application

	private val localPreferencesUseCase = LocalPreferencesUseCase(LocalPreferencesRepositoryImpl())

	private val auctionHttpUseCase =
		AuctionHttpUseCase(AuctionHttpRepositoryImpl.getInstance(NetworkClient.getWoWClient))

	private val realmDatabaseUseCase =
		RealmDatabaseUseCase(RealmDatabaseRepositoryImpl(application))

	private val itemHttpUseCase =
		ItemHttpUseCase(ItemHttpRepositoryImpl.getInstance(NetworkClient.getWoWClient))

	private var _auctionsViewState = AuctionsViewState()
	val auctionsViewState = MutableLiveData<AuctionsViewState>()

	private val _itemSearchResults: MutableLiveData<Quadruple<List<Auction>, List<ItemSearchResponse.Result>, AuctionRealmData, Boolean>> =
		MutableLiveData()
	val itemSearchResults: LiveData<Quadruple<List<Auction>, List<ItemSearchResponse.Result>, AuctionRealmData, Boolean>>
		get() = _itemSearchResults

	private var gson = Gson()

	data class AuctionsViewState(
		val version: Int = GONE,
		val realm: Int = GONE,
		val faction: Int = GONE,
		val keyword: Int = GONE,
		val category: Int = GONE,
		val openFilter: Int = -1,
		val search: Int = GONE,
		val loadingFromCache: Int = GONE,
		val loadingFromServer: Int = GONE,
		val fab: Int = GONE
	)

	init {
		recallAuctions()
		handleSearchBtnVisibility()
	}

	fun onFilterClick(pos: Int) {
		val fabViewState = _auctionsViewState.fab

		if (pos == _auctionsViewState.openFilter)
			_auctionsViewState = AuctionsViewState(fab = fabViewState)
		else {
			when (pos) {
				0 -> _auctionsViewState =
					AuctionsViewState(fab = fabViewState, version = VISIBLE, openFilter = pos)
				1 -> _auctionsViewState =
					AuctionsViewState(fab = fabViewState, realm = VISIBLE, openFilter = pos)
				2 -> _auctionsViewState =
					AuctionsViewState(fab = fabViewState, faction = VISIBLE, openFilter = pos)
				3 -> _auctionsViewState =
					AuctionsViewState(fab = fabViewState, keyword = VISIBLE, openFilter = pos)
				4 -> _auctionsViewState =
					AuctionsViewState(fab = fabViewState, category = VISIBLE, openFilter = pos)
			}
		}

		handleSearchBtnVisibility()
	}

	fun onVersionApplyClick(index: Int) {
		if (index in 0..1)
			localPreferencesUseCase.setSelectedVersionFilter(index)
	}

	fun onRealmApplyClick(realmJson: String) {
		localPreferencesUseCase.setSelectedRealmJson(realmJson)
	}

	fun onFactionApplyClick(index: Int) {
		if (index in 0..2)
			localPreferencesUseCase.setSelectedFaction(index)
	}

	fun onKeywordApplyClick(keyword: String) {
		localPreferencesUseCase.setItemKeyword(keyword)
	}

	fun onCategoryApplyClick(selectedCategoriesWrapper: SelectedCategoriesWrapper) {
		localPreferencesUseCase.setSelectedCategoriesJson(Gson().toJson(selectedCategoriesWrapper))
	}

	fun onSearchClick() {
		viewModelScope.launch {
			val ctx = ClassicAuctions.applicationContext()
			val aqs = setupQuerySettings(ctx)
			val file = updateAuctions(aqs)
			filterAuctions(ctx, aqs, file)

			_auctionsViewState = AuctionsViewState(search = VISIBLE, fab = VISIBLE)
			auctionsViewState.value = _auctionsViewState
		}
	}

	private fun handleSearchBtnVisibility() {
		val selectedVersion = localPreferencesUseCase.getSelectedVersion()
		val selectedRealm =
			gson.fromJson(localPreferencesUseCase.getSelectedRealmJson(), Realm::class.java)
		val selectedFaction = localPreferencesUseCase.getSelectedFaction()
		val selectedKeyword = localPreferencesUseCase.getItemKeyword()

		_auctionsViewState = if (selectedVersion > -1 &&
			selectedRealm != null &&
			selectedFaction > -1 &&
			selectedKeyword != String.empty()
		)
			_auctionsViewState.copy(search = VISIBLE)
		else
			_auctionsViewState.copy(search = GONE)

		auctionsViewState.value = _auctionsViewState
	}

	fun onFilterClickClose() {
		_auctionsViewState = AuctionsViewState(fab = _auctionsViewState.fab)
		handleSearchBtnVisibility()
	}

	private suspend fun setupQuerySettings(ctx: Context): AuctionQuerySettings {
		val selectedRealm =
			gson.fromJson(localPreferencesUseCase.getSelectedRealmJson(), Realm::class.java)

		val selectedVersion = localPreferencesUseCase.getSelectedVersion()
		val selectedFaction = localPreferencesUseCase.getSelectedFaction()

		var factionAuctionHouseId: Int = -1
		var namespace = String.empty()
		var clusterId = selectedRealm.clusterId
		val region = selectedRealm.region

		// Set faction.
		when (selectedFaction) {
			0 -> factionAuctionHouseId = 2
			1 -> factionAuctionHouseId = 6
			2 -> factionAuctionHouseId = 7
		}

		// Setup namespace.
		when {
			selectedRealm.category == ctx.getString(R.string.seasonal) -> {
				namespace = if (region == String.eu())
					localPreferencesUseCase.getDynamicEraEu()
				else
					localPreferencesUseCase.getDynamicEraUs()
			}
			region == String.eu() -> {
				when (selectedVersion) {
					0 -> namespace = localPreferencesUseCase.getDynamicEraEu()
					1 -> namespace = localPreferencesUseCase.getDynamicProgEu()
				}
			}
			else -> {
				when (selectedVersion) {
					0 -> namespace = localPreferencesUseCase.getDynamicEraUs()
					1 -> namespace = localPreferencesUseCase.getDynamicProgUs()
				}
			}
		}

		// Get correct clusterId if Era is selected.
		if (selectedVersion == 0) {
			clusterId = realmDatabaseUseCase.getEraClusterIdByName(
				selectedRealm.name,
				ctx.getString(R.string.classic_era)
			)

			if (clusterId == 0)
				clusterId =
					realmDatabaseUseCase.getRussianEraClusterIdByName(selectedRealm.name).last()
		}

		return AuctionQuerySettings(
			selectedRealm,
			selectedVersion,
			selectedFaction,
			factionAuctionHouseId,
			namespace,
			clusterId,
			region
		)
	}

	/**
	 * Updates the auction list if it has expired and returns the file.
	 * @return A file with an up-to-date auction list.
	 * */
	private suspend fun updateAuctions(aqs: AuctionQuerySettings): File {
		var shouldUpdateAuctions = true
		val files = FileUtils.listFiles()

		// Prepare filename (will be replaced if auctions haven't expired yet).
		val calendar = Calendar.getInstance()
		calendar.add(Calendar.HOUR, 1)
		var filename =
			"${calendar.timeInMillis}_${aqs.selectedRealm.name}_${aqs.namespace}_${aqs.factionAuctionHouseId}.json"

		// Check if the current settings match any existing file.
		files?.forEach {
			if (it.name.contains(aqs.selectedRealm.name) &&
				it.name.contains(aqs.namespace) &&
				it.name.contains("${aqs.factionAuctionHouseId}.json")
			) {
				// Match found, check if it has expired.
				val expiration = it.name.substring(0, it.name.indexOf("_")).toLong()

				shouldUpdateAuctions = System.currentTimeMillis() >= expiration

				if (!shouldUpdateAuctions)
					filename = it.name

				return@forEach
			}
		}

		// True if the file does not exist or has expired.
		if (shouldUpdateAuctions) {
			_auctionsViewState = AuctionsViewState(search = VISIBLE, loadingFromServer = VISIBLE)
			auctionsViewState.postValue(_auctionsViewState)

			getAuctions(
				aqs.selectedRealm.region,
				aqs.clusterId,
				aqs.factionAuctionHouseId,
				aqs.namespace,
				File(app.filesDir, filename)
			)
		} else {
			_auctionsViewState = AuctionsViewState(search = VISIBLE, loadingFromCache = VISIBLE)
			auctionsViewState.postValue(_auctionsViewState)
		}

		return File(app.filesDir, filename)
	}

	private suspend fun filterAuctions(ctx: Context, aqs: AuctionQuerySettings, cache: File) {
		itemHttpUseCase.itemSearch(localPreferencesUseCase.getItemKeyword()).either(
			success = { resp ->
				val filteredAuctions = mutableListOf<Auction>()

				val reader = FileUtils.read(cache)
				val auctions: Array<Auction> =
					Gson().fromJson(reader, Array<Auction>::class.java)

				val selectedCategoriesWrapper = gson.fromJson(
					localPreferencesUseCase.getSelectedCategoriesJson(),
					SelectedCategoriesWrapper::class.java
				)

				var noCategoriesSelected = true
				selectedCategoriesWrapper.selectedCategories.forEach { category ->
					if (category.subCategoryIds.isNotEmpty())
						noCategoriesSelected = false
				}

				auctions.forEach outer@{ auction ->
					resp.results.forEach inner@{ item ->
						if (auction.item.id == item.data.id) {
							if (noCategoriesSelected) {
								filteredAuctions.add(auction)
								return@inner
							}

							if (selectedCategoriesWrapper.selectedCategories.isNotEmpty()) {
								selectedCategoriesWrapper.selectedCategories.forEach { category ->
									if (category.categoryId == item.data.itemClass.id &&
										category.subCategoryIds.contains(item.data.itemSubClass.id)
									) {
										filteredAuctions.add(auction)
									}
								}
							}
						}
					}
				}

				saveAuctionQueryResults(ctx, aqs, filteredAuctions, resp.results)
			},
			failure = {}
		)
	}

	private fun saveAuctionQueryResults(
		ctx: Context,
		aqs: AuctionQuerySettings,
		filteredAuctions: List<Auction>,
		itemsFound: List<ItemSearchResponse.Result>
	) {
		val auctionRealmData = AuctionRealmData()
		auctionRealmData.name = aqs.selectedRealm.name
		auctionRealmData.region = aqs.region

		when (aqs.selectedVersion) {
			0 -> auctionRealmData.version = ctx.getString(R.string.era)
			1 -> auctionRealmData.version = ctx.getString(R.string.prog_abv)
		}

		when (aqs.selectedFaction) {
			0 -> auctionRealmData.faction = ctx.getString(R.string.alliance)
			1 -> auctionRealmData.faction = ctx.getString(R.string.horde)
			2 -> auctionRealmData.faction = ctx.getString(R.string.neutral)
		}

		val auctionsFile = ctx.getString(R.string.last_auction_auctions_file)
		val itemsFile = ctx.getString(R.string.last_auction_items_file)
		val realmFile = ctx.getString(R.string.last_auction_realm_file)

		FileUtils.write(File(app.filesDir, auctionsFile), filteredAuctions)
		FileUtils.write(File(app.filesDir, itemsFile), itemsFound)
		FileUtils.write(File(app.filesDir, realmFile), auctionRealmData)

		_itemSearchResults.postValue(
			Quadruple(
				filteredAuctions.toList(),
				itemsFound,
				auctionRealmData,
				true
			)
		)
	}

	private suspend fun getAuctions(
		region: String,
		clusterId: Int,
		factionAuctionHouseId: Int,
		namespace: String,
		file: File
	) {
		auctionHttpUseCase.getAuctions(
			region,
			clusterId,
			factionAuctionHouseId,
			namespace
		).either(
			success = { resp ->
				FileUtils.write(file, resp.auctions)
			},
			failure = {}
		)
	}

	private fun recallAuctions() {
		val ctx = ClassicAuctions.applicationContext()

		val files = mutableListOf<File>()
		files.add(File(app.filesDir, ctx.getString(R.string.last_auction_auctions_file)))
		files.add(File(app.filesDir, ctx.getString(R.string.last_auction_items_file)))
		files.add(File(app.filesDir, ctx.getString(R.string.last_auction_realm_file)))

		files.forEach {
			if (!FileUtils.doesFileExist(it))
				return
		}

		// Populate RecyclerView.
		var reader = FileUtils.read(files.first())
		val auctions: Array<Auction> = Gson().fromJson(reader, Array<Auction>::class.java)

		reader = FileUtils.read(files[1])
		val items: Array<ItemSearchResponse.Result> =
			Gson().fromJson(reader, Array<ItemSearchResponse.Result>::class.java)

		reader = FileUtils.read(files.last())
		val auctionRealmData: AuctionRealmData =
			Gson().fromJson(reader, AuctionRealmData::class.java)

		_itemSearchResults.value =
			Quadruple(auctions.asList(), items.asList(), auctionRealmData, false)
	}

	fun showFab(showFab: Boolean) {
		_auctionsViewState = if (showFab)
			AuctionsViewState(fab = VISIBLE, search = VISIBLE)
		else
			AuctionsViewState(fab = GONE, search = VISIBLE)

		auctionsViewState.postValue(_auctionsViewState)
	}
}