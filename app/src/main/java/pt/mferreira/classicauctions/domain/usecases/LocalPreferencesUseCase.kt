package pt.mferreira.classicauctions.domain.usecases

import pt.mferreira.classicauctions.domain.repositories.LocalPreferencesRepository

class LocalPreferencesUseCase(private val repository: LocalPreferencesRepository) {
	fun getDynamicProgEu(): String = repository.getDynamicProgEu()
	fun getDynamicProgUs(): String = repository.getDynamicProgUs()
	fun getDynamicEraEu(): String = repository.getDynamicEraEu()
	fun getDynamicEraUs(): String = repository.getDynamicEraUs()

	fun getStaticProgEu(): String = repository.getStaticProgEu()
	fun getStaticProgUs(): String = repository.getStaticProgUs()
	fun getStaticEraEu(): String = repository.getStaticEraEu()
	fun getStaticEraUs(): String = repository.getStaticEraUs()

	fun setIsSetupComplete(isSetupComplete: Boolean) =
		repository.setIsSetupComplete(isSetupComplete)

	fun getIsSetupComplete(): Boolean = repository.getIsSetupComplete()

	fun setNextRefresh(nextRefreshMillis: Long) = repository.setNextRefresh(nextRefreshMillis)
	fun getNextRefresh(): Long = repository.getNextRefresh()

	fun setToken(token: String) = repository.setToken(token)
	fun getToken(): String = repository.getToken()

	fun setTokenExpirationDate(tokenExpirationDate: Long) =
		repository.setTokenExpirationDate(tokenExpirationDate)

	fun getTokenExpirationDate(): Long = repository.getTokenExpirationDate()

	fun setSelectedVersionFilter(selectedVersion: Int) =
		repository.setSelectedVersion(selectedVersion)

	fun getSelectedVersion(): Int = repository.getSelectedVersion()

	fun setSelectedRealmJson(selectedRealmJson: String) =
		repository.setSelectedRealmJson(selectedRealmJson)

	fun getSelectedRealmJson(): String = repository.getSelectedRealmJson()

	fun setSelectedFaction(selectedFaction: Int) = repository.setSelectedFaction(selectedFaction)
	fun getSelectedFaction(): Int = repository.getSelectedFaction()

	fun setSelectedRealmRegion(selectedRealmRegion: String) =
		repository.setSelectedRealmRegion(selectedRealmRegion)

	fun getSelectedRealmRegion(): String = repository.getSelectedRealmRegion()

	fun setSelectedSeason(selectedSeason: Int) = repository.setSelectedSeason(selectedSeason)
	fun getSelectedSeason(): Int = repository.getSelectedSeason()

	fun setSelectedPvPRegionKey(selectedPvPRegionKey: Int) =
		repository.setSelectedPvPRegionKey(selectedPvPRegionKey)

	fun getSelectedPvPRegionKey(): Int = repository.getSelectedPvPRegionKey()

	fun setItemKeyword(keyword: String) = repository.setItemKeyword(keyword)
	fun getItemKeyword(): String = repository.getItemKeyword()

	fun setSelectedCategoriesJson(selectedCategoriesJson: String) =
		repository.setSelectedCategoriesJson(selectedCategoriesJson)

	fun getSelectedCategoriesJson(): String = repository.getSelectedCategoriesJson()
}