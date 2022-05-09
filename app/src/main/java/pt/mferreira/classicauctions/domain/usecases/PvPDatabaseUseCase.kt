package pt.mferreira.classicauctions.domain.usecases

import pt.mferreira.classicauctions.data.entities.PvPEntry
import pt.mferreira.classicauctions.domain.repositories.PvPDatabaseRepository

class PvPDatabaseUseCase(private val repository: PvPDatabaseRepository) {
	suspend fun insert(pvpEntry: PvPEntry) = repository.insert(pvpEntry)
	suspend fun insertAll(pvpEntries: List<PvPEntry>) = repository.insertAll(pvpEntries)
	suspend fun getAll(): List<PvPEntry> = repository.getAll()
	suspend fun getByKey(key: Int) = repository.getByKey(key)
	suspend fun getByData(region: String, pvpRegion: Int, season: Int) =
		repository.getByData(region, pvpRegion, season)

	suspend fun getSeasonsByRegionId(region: String, pvpRegionId: Int) =
		repository.getSeasonsByRegionId(region, pvpRegionId)
}