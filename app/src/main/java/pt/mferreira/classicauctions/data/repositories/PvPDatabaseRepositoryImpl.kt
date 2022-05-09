package pt.mferreira.classicauctions.data.repositories

import android.app.Application
import pt.mferreira.classicauctions.data.databases.AppDatabase
import pt.mferreira.classicauctions.data.entities.PvPEntry
import pt.mferreira.classicauctions.domain.repositories.PvPDatabaseRepository

class PvPDatabaseRepositoryImpl(application: Application) : PvPDatabaseRepository {
	private val pvpDao = AppDatabase.getInstance(application).pvpDao()

	override suspend fun insert(pvpEntry: PvPEntry) {
		pvpDao.insert(pvpEntry)
	}

	override suspend fun insertAll(pvpEntries: List<PvPEntry>) {
		pvpDao.insertAll(pvpEntries)
	}

	override suspend fun getAll(): List<PvPEntry> {
		return pvpDao.getAll()
	}

	override suspend fun getByKey(key: Int): PvPEntry {
		return pvpDao.getByKey(key)
	}

	override suspend fun getByData(region: String, pvpRegion: Int, season: Int): PvPEntry {
		return pvpDao.getByData(region, pvpRegion, season)
	}

	override suspend fun getSeasonsByRegionId(region: String, pvpRegionId: Int): List<Int> {
		return pvpDao.getSeasonsByRegionId(region, pvpRegionId)
	}

	companion object {
		@Volatile
		private var instance: PvPDatabaseRepositoryImpl? = null

		@JvmStatic
		fun getInstance(application: Application) =
			instance ?: synchronized(this) {
				instance ?: PvPDatabaseRepositoryImpl(application).also { instance = it }
			}
	}
}