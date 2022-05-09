package pt.mferreira.classicauctions.domain.repositories

interface LocalPreferencesRepository {
	fun getDynamicProgEu(): String
	fun getDynamicProgUs(): String
	fun getDynamicEraEu(): String
	fun getDynamicEraUs(): String

	fun getStaticProgEu(): String
	fun getStaticProgUs(): String
	fun getStaticEraEu(): String
	fun getStaticEraUs(): String

	fun setIsSetupComplete(isSetupComplete: Boolean)
	fun getIsSetupComplete(): Boolean

	fun setNextRefresh(nextRefreshMillis: Long)
	fun getNextRefresh(): Long

	fun setToken(token: String)
	fun getToken(): String

	fun setTokenExpirationDate(tokenExpirationDate: Long)
	fun getTokenExpirationDate(): Long

	fun setSelectedVersion(selectedVersion: Int)
	fun getSelectedVersion(): Int

	// Takes a Realm converted to a JSON string.
	fun setSelectedRealmJson(selectedRealmNameJson: String)
	fun getSelectedRealmJson(): String

	fun setSelectedFaction(selectedFaction: Int)
	fun getSelectedFaction(): Int

	fun setSelectedRealmRegion(selectedRealmRegion: String)
	fun getSelectedRealmRegion(): String

	fun setSelectedSeason(selectedSeason: Int)
	fun getSelectedSeason(): Int

	fun setSelectedPvPRegionKey(selectedPvPRegionKey: Int)
	fun getSelectedPvPRegionKey(): Int

	fun setItemKeyword(keyword: String)
	fun getItemKeyword(): String

	// Takes a SelectedCategory converted to a JSON string.
	fun setSelectedCategoriesJson(selectedCategoriesJson: String)
	fun getSelectedCategoriesJson(): String
}