package pt.mferreira.classicauctions.domain.repositories

import pt.mferreira.classicauctions.data.entities.PvPEntry

interface PvPDatabaseRepository {
	suspend fun insert(pvpEntry: PvPEntry)
	suspend fun insertAll(pvpEntries: List<PvPEntry>)
	suspend fun getAll(): List<PvPEntry>
	suspend fun getByKey(key: Int): PvPEntry
	suspend fun getByData(region: String, pvpRegion: Int, season: Int): PvPEntry
	suspend fun getSeasonsByRegionId(region: String, pvpRegionId: Int): List<Int>
}