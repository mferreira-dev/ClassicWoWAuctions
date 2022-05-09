package pt.mferreira.classicauctions.presentation.container

import android.app.Application
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import pt.mferreira.classicauctions.data.entities.*
import pt.mferreira.classicauctions.data.network.NetworkClient
import pt.mferreira.classicauctions.data.repositories.*
import pt.mferreira.classicauctions.domain.entities.BracketsWrapper
import pt.mferreira.classicauctions.domain.entities.SelectedCategoriesWrapper
import pt.mferreira.classicauctions.domain.usecases.*
import pt.mferreira.classicauctions.utils.extensions.eu
import pt.mferreira.classicauctions.utils.extensions.us
import java.util.*

class ContainerViewModel(application: Application) : AndroidViewModel(application) {
	private var app = application

	private val localPreferencesUseCase =
		LocalPreferencesUseCase(LocalPreferencesRepositoryImpl.getInstance())

	private val realmHttpUseCase = RealmHttpUseCase(
		RealmHttpRepositoryImpl.getInstance(NetworkClient.getWoWClient)
	)

	private val itemHttpUseCase = ItemHttpUseCase(
		ItemHttpRepositoryImpl.getInstance(NetworkClient.getWoWClient)
	)

	private val classHttpUseCase = ClassHttpUseCase(
		ClassHttpRepositoryImpl.getInstance(NetworkClient.getWoWClient)
	)

	private val pvpHttpUseCase = PvPHttpUseCase(
		PvPHttpRepositoryImpl.getInstance(NetworkClient.getWoWClient)
	)

	private val itemDatabaseUseCase = ItemDatabaseUseCase(
		ItemDatabaseRepositoryImpl.getInstance(app)
	)

	private val realmDatabaseUseCase = RealmDatabaseUseCase(
		RealmDatabaseRepositoryImpl.getInstance(app)
	)

	private val classDatabaseUseCase = ClassDatabaseUseCase(
		ClassDatabaseRepositoryImpl.getInstance(app)
	)

	private val pvpDatabaseUseCase = PvPDatabaseUseCase(
		PvPDatabaseRepositoryImpl.getInstance(app)
	)

	private var _welcomeViewState = ContainerViewState()
	val welcomeViewState = MutableLiveData<ContainerViewState>()

	data class ContainerViewState(
		val loading: Int = GONE
	)

	init {
		viewModelScope.launch {
			if (!localPreferencesUseCase.getIsSetupComplete()) {
				_welcomeViewState = ContainerViewState(VISIBLE)
				welcomeViewState.postValue(_welcomeViewState)

				coroutineScope {
					async {
						fetchAndCacheCategories()
						fetchAndCacheClasses()
					}
				}

				localPreferencesUseCase.setIsSetupComplete(true)
			}

			if (shouldUpdateRealms()) {
				_welcomeViewState = ContainerViewState(VISIBLE)
				welcomeViewState.postValue(_welcomeViewState)

				coroutineScope {
					async {
						fetchAndCacheRealms()
						fetchAndCachePvPData()
					}
				}

				setNextRefreshDate()
			}

			_welcomeViewState = ContainerViewState()
			welcomeViewState.postValue(_welcomeViewState)
		}
	}

	private fun shouldUpdateRealms(): Boolean {
		val nextRefresh = localPreferencesUseCase.getNextRefresh()
		return nextRefresh == 0L || System.currentTimeMillis() >= nextRefresh
	}

	private suspend fun fetchAndCacheRealms() {
		val triples = listOf(
			Triple(mutableListOf<Int>(), localPreferencesUseCase.getDynamicProgEu(), String.eu()),
			Triple(mutableListOf(), localPreferencesUseCase.getDynamicProgUs(), String.us()),
			Triple(mutableListOf(), localPreferencesUseCase.getDynamicEraEu(), String.eu()),
			Triple(mutableListOf(), localPreferencesUseCase.getDynamicEraUs(), String.us())
		)

		coroutineScope {
			triples.forEach { triple ->
				async {
					fetchRealmIds(
						triple.third,
						triple.second
					).forEach {
						triple.first.add(it)
					}
				}
			}
		}

		coroutineScope {
			triples.forEach { triple ->
				triple.first.forEach {
					async {
						realmDatabaseUseCase.insertAll(
							fetchRealmDetails(triple.third, it, triple.second)
						)
					}
				}
			}
		}
	}

	private suspend fun fetchRealmIds(
		region: String,
		namespace: String
	): List<Int> {
		val list = mutableListOf<Int>()

		realmHttpUseCase.getRealmIndex(region, namespace).either(
			success = {
				it.realms.forEach {
					val href = it.href
					val id = href.substring(
						href.lastIndexOf("/") + 1,
						href.lastIndexOf("?")
					)
					list.add(Integer.parseInt(id))
				}
			},
			failure = {}
		)

		return list
	}

	private suspend fun fetchRealmDetails(
		region: String,
		realmId: Int,
		namespace: String
	): List<Realm> {
		val list = mutableListOf<Realm>()

		realmHttpUseCase.getRealmDetails(region, realmId, namespace).either(
			success = { resp ->
				resp.realms.forEach { realm ->
					val formattedName = if (realm.name.contains("("))
						realm.name.substring(0, realm.name.indexOf("(") - 1)
					else
						realm.name

					list.add(
						Realm(
							0,
							realm.category,
							realm.id,
							resp.id,
							formattedName,
							region,
							realm.type.name
						)
					)
				}
			},
			failure = {}
		)

		return list
	}

	private fun setNextRefreshDate() {
		val calendar = Calendar.getInstance()

		var difference = Calendar.WEDNESDAY - calendar.get(Calendar.DAY_OF_WEEK)
		if (difference <= 0) difference += 7

		calendar.apply {
			add(Calendar.DAY_OF_WEEK, difference)
			set(Calendar.HOUR_OF_DAY, 0)
			set(Calendar.MINUTE, 0)
			set(Calendar.MILLISECOND, 0)
		}

		localPreferencesUseCase.setNextRefresh(calendar.timeInMillis)
	}

	private suspend fun fetchAndCacheCategories() {
		coroutineScope {
			async {
				val categories = fetchItemCategories()

				// Prepare data required for category filter use.
				val selectedCategoriesWrapper = SelectedCategoriesWrapper(mutableListOf())
				categories.forEach { itemCategory ->
					selectedCategoriesWrapper.selectedCategories.add(
						SelectedCategoriesWrapper.SelectedCategory(
							itemCategory.id, mutableListOf()
						)
					)
				}

				localPreferencesUseCase.setSelectedCategoriesJson(
					Gson().toJson(
						selectedCategoriesWrapper
					)
				)
				itemDatabaseUseCase.insertAllItemCategories(categories)
			}
		}

		coroutineScope {
			itemDatabaseUseCase.getAllItemCategories().forEach {
				async {
					val subCategories = mutableListOf<ItemSubCategory>()
					subCategories.addAll(fetchItemSubCategories(it))

					// Handle 1H and 2H weapons.
					for (i in subCategories.indices) {
						if (i + 1 <= subCategories.size) {
							for (j in i + 1 until subCategories.size) {
								if (subCategories[i].name == subCategories[j].name) {
									subCategories[i] =
										subCategories[i].copy(name = "${subCategories[i].name} (1H)")

									subCategories[j] =
										subCategories[j].copy(name = "${subCategories[j].name} (2H)")
								}
							}
						}
					}

					itemDatabaseUseCase.insertAllItemSubCategories(subCategories)
				}
			}
		}
	}

	private suspend fun fetchItemCategories(): List<ItemCategory> {
		val list = mutableListOf<ItemCategory>()

		itemHttpUseCase.getItemCategories().either(
			success = {
				it.categories.forEach {
					if (!it.name.contains("Token"))
						list.add(ItemCategory(0, it.id, it.name))
				}
			},
			failure = {}
		)

		return list
	}

	private suspend fun fetchItemSubCategories(category: ItemCategory): List<ItemSubCategory> {
		val list = mutableListOf<ItemSubCategory>()

		itemHttpUseCase.getItemSubCategories(category.id).either(
			success = {
				it.categories.forEach {
					if (it.name != category.name)
						list.add(ItemSubCategory(0, category.id, it.id, it.name))
				}
			},
			failure = {}
		)

		return list
	}

	private suspend fun fetchAndCacheClasses() {
		val tempClasses = mutableListOf<Pair<String, Int>>()
		val finalClasses = mutableListOf<Class>()

		coroutineScope {
			async {
				classHttpUseCase.getClassIndex().either(
					success = { resp ->
						resp.classes.forEach { aClass ->
							tempClasses.add(Pair(aClass.name, aClass.id))
						}
					},
					failure = {}
				)
			}
		}

		coroutineScope {
			tempClasses.forEach { tempClass ->
				async {
					classHttpUseCase.getClassMedia(tempClass.second).either(
						success = { resp ->
							finalClasses.add(
								Class(0, tempClass.second, tempClass.first, resp.assets.first().url)
							)
						},
						failure = {}
					)
				}
			}
		}

		classDatabaseUseCase.insertAll(finalClasses)
	}

	private suspend fun fetchAndCachePvPData() {
		val euPvPRegionIds = mutableListOf<Int>()
		val usPvPRegionIds = mutableListOf<Int>()

		val euPvPRegionSeasonIds = mutableListOf<Pair<Int, Int>>()
		val usPvPRegionSeasonIds = mutableListOf<Pair<Int, Int>>()

		val euEntries = mutableListOf<PvPEntry>()
		val usEntries = mutableListOf<PvPEntry>()

		coroutineScope {
			async {
				euPvPRegionIds.addAll(fetchPvPRegionIds(String.eu()))
				usPvPRegionIds.addAll(fetchPvPRegionIds(String.us()))
			}
		}

		coroutineScope {
			async {
				euPvPRegionIds.forEach { regionId ->
					async {
						fetchPvPRegionSeasonIds(regionId, String.eu()).forEach { seasonId ->
							euPvPRegionSeasonIds.add(Pair(regionId, seasonId))
						}
					}
				}

				usPvPRegionIds.forEach { regionId ->
					async {
						fetchPvPRegionSeasonIds(regionId, String.us()).forEach { seasonId ->
							usPvPRegionSeasonIds.add(Pair(regionId, seasonId))
						}
					}
				}
			}
		}

		coroutineScope {
			async {
				euPvPRegionSeasonIds.forEach {
					async {
						euEntries.add(
							PvPEntry(
								0,
								String.eu(),
								it.first,
								it.second,
								fetchBrackets(it.first, it.second, String.eu())
							)
						)
					}
				}

				usPvPRegionSeasonIds.forEach {
					async {
						usEntries.add(
							PvPEntry(
								0,
								String.us(),
								it.first,
								it.second,
								fetchBrackets(it.first, it.second, String.us())
							)
						)
					}
				}
			}
		}

		pvpDatabaseUseCase.insertAll(euEntries)
		pvpDatabaseUseCase.insertAll(usEntries)
	}

	private suspend fun fetchPvPRegionIds(region: String): MutableList<Int> {
		val pvpRegionIds = mutableListOf<Int>()

		pvpHttpUseCase.getPvPRegionIndex(region).either(
			success = { resp ->
				resp.pvpRegions.forEach {
					val url = it.href

					val id = url.substring(
						url.indexOf("pvp-region") + 11,
						url.indexOf("/pvp-season")
					).toInt()

					pvpRegionIds.add(id)
				}
			},
			failure = {}
		)

		return pvpRegionIds
	}

	private suspend fun fetchPvPRegionSeasonIds(
		pvpRegionId: Int,
		region: String
	): MutableList<Int> {
		val seasonIds = mutableListOf<Int>()

		pvpHttpUseCase.getPvPRegionSeasonIndex(pvpRegionId, region).either(
			success = { resp ->
				resp.seasons.forEach {
					seasonIds.add(it.id)
				}
			},
			failure = {}
		)

		return seasonIds
	}

	private suspend fun fetchBrackets(
		pvpRegionId: Int,
		pvpSeasonId: Int,
		region: String
	): String {
		val brackets = BracketsWrapper(mutableListOf())

		pvpHttpUseCase.getLeaderboardIndex(pvpRegionId, pvpSeasonId, region).either(
			success = { resp ->
				resp.leaderboards.forEach {
					brackets.brackets.add(BracketsWrapper.Bracket(it.id, it.name))
				}
			},
			failure = {}
		)

		return Gson().toJson(brackets)
	}
}