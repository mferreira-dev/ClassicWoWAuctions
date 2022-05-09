package pt.mferreira.classicauctions.data.repositories

import android.content.Context
import android.content.SharedPreferences
import pt.mferreira.classicauctions.ClassicAuctions
import pt.mferreira.classicauctions.R
import pt.mferreira.classicauctions.domain.repositories.LocalPreferencesRepository
import pt.mferreira.classicauctions.utils.extensions.empty

class LocalPreferencesRepositoryImpl : LocalPreferencesRepository {
	private val sharedPreferences: SharedPreferences by lazy {
		ClassicAuctions.applicationContext()
			.getSharedPreferences("preferences", Context.MODE_PRIVATE)
	}

	override fun getDynamicProgEu(): String {
		return ClassicAuctions.applicationContext().getString(R.string.dynamic_prog_eu)
	}

	override fun getDynamicProgUs(): String {
		return ClassicAuctions.applicationContext().getString(R.string.dynamic_prog_us)
	}

	override fun getDynamicEraEu(): String {
		return ClassicAuctions.applicationContext().getString(R.string.dynamic_era_eu)
	}

	override fun getDynamicEraUs(): String {
		return ClassicAuctions.applicationContext().getString(R.string.dynamic_era_us)
	}

	override fun getStaticProgEu(): String {
		return ClassicAuctions.applicationContext().getString(R.string.static_prog_eu)
	}

	override fun getStaticProgUs(): String {
		return ClassicAuctions.applicationContext().getString(R.string.static_prog_us)
	}

	override fun getStaticEraEu(): String {
		return ClassicAuctions.applicationContext().getString(R.string.static_era_eu)
	}

	override fun getStaticEraUs(): String {
		return ClassicAuctions.applicationContext().getString(R.string.static_era_us)
	}

	override fun setIsSetupComplete(isSetupComplete: Boolean) {
		sharedPreferences.edit().putBoolean(SETUP_COMPLETE, isSetupComplete).apply()
	}

	override fun getIsSetupComplete(): Boolean {
		return sharedPreferences.getBoolean(SETUP_COMPLETE, false)
	}

	override fun setNextRefresh(nextRefreshMillis: Long) {
		sharedPreferences.edit().putLong(NEXT_REFRESH, nextRefreshMillis).apply()
	}

	override fun getNextRefresh(): Long {
		return sharedPreferences.getLong(NEXT_REFRESH, 0)
	}

	override fun setToken(token: String) {
		sharedPreferences.edit().putString(TOKEN, token).apply()
	}

	override fun getToken(): String {
		return sharedPreferences.getString(TOKEN, String.empty()).orEmpty()
	}

	override fun setTokenExpirationDate(tokenExpirationDate: Long) {
		sharedPreferences.edit().putLong(TOKEN_EXPIRATION_DATE, tokenExpirationDate).apply()
	}

	override fun getTokenExpirationDate(): Long {
		return sharedPreferences.getLong(TOKEN_EXPIRATION_DATE, 0)
	}

	override fun setSelectedVersion(selectedVersion: Int) {
		sharedPreferences.edit().putInt(SELECTED_VERSION, selectedVersion).apply()
	}

	override fun getSelectedVersion(): Int {
		return sharedPreferences.getInt(SELECTED_VERSION, -1)
	}

	override fun setSelectedRealmJson(selectedRealmNameJson: String) {
		sharedPreferences.edit().putString(SELECTED_REALM_JSON, selectedRealmNameJson).apply()
	}

	override fun getSelectedRealmJson(): String {
		return sharedPreferences.getString(SELECTED_REALM_JSON, String.empty()).orEmpty()
	}

	override fun setSelectedFaction(selectedFaction: Int) {
		sharedPreferences.edit().putInt(SELECTED_FACTION, selectedFaction).apply()
	}

	override fun getSelectedFaction(): Int {
		return sharedPreferences.getInt(SELECTED_FACTION, -1)
	}

	override fun setSelectedRealmRegion(selectedRealmRegion: String) {
		sharedPreferences.edit().putString(SELECTED_REALM_REGION, selectedRealmRegion).apply()
	}

	override fun getSelectedRealmRegion(): String {
		return sharedPreferences.getString(SELECTED_REALM_REGION, String.empty()).orEmpty()
	}

	override fun setSelectedSeason(selectedSeason: Int) {
		sharedPreferences.edit().putInt(SELECTED_SEASON, selectedSeason).apply()
	}

	override fun getSelectedSeason(): Int {
		return sharedPreferences.getInt(SELECTED_SEASON, -1)
	}

	override fun setSelectedPvPRegionKey(selectedPvPRegionKey: Int) {
		sharedPreferences.edit().putInt(SELECTED_PVP_REGION_KEY, selectedPvPRegionKey).apply()
	}

	override fun getSelectedPvPRegionKey(): Int {
		return sharedPreferences.getInt(SELECTED_PVP_REGION_KEY, -1)
	}

	override fun setItemKeyword(keyword: String) {
		sharedPreferences.edit().putString(ITEM_KEYWORD, keyword).apply()
	}

	override fun getItemKeyword(): String {
		return sharedPreferences.getString(ITEM_KEYWORD, String.empty()).orEmpty()
	}

	override fun setSelectedCategoriesJson(selectedCategoriesJson: String) {
		sharedPreferences.edit().putString(SELECTED_CATEGORIES_JSON, selectedCategoriesJson).apply()
	}

	override fun getSelectedCategoriesJson(): String {
		return sharedPreferences.getString(SELECTED_CATEGORIES_JSON, String.empty()).orEmpty()
	}

	companion object {
		private const val SETUP_COMPLETE = "setup_complete"
		private const val NEXT_REFRESH = "next_refresh"

		private const val TOKEN = "token"
		private const val TOKEN_EXPIRATION_DATE = "token_expiration_date"

		private const val SELECTED_VERSION = "selected_version"
		private const val SELECTED_REALM_JSON = "selected_realm_name_json"
		private const val SELECTED_FACTION = "selected_faction"
		private const val SELECTED_REALM_REGION = "selected_realm_region"
		private const val SELECTED_PVP_REGION_KEY = "selected_pvp_region_key"
		private const val ITEM_KEYWORD = "item_keyword"
		private const val SELECTED_CATEGORIES_JSON = "selected_categories_json"
		private const val SELECTED_SEASON = "selected_season"

		@Volatile
		private var instance: LocalPreferencesRepositoryImpl? = null

		@JvmStatic
		fun getInstance() = instance ?: synchronized(this) {
			instance ?: LocalPreferencesRepositoryImpl().also { instance = it }
		}
	}
}