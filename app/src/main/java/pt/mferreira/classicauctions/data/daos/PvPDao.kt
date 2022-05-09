package pt.mferreira.classicauctions.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pt.mferreira.classicauctions.data.entities.PvPEntry

@Dao
interface PvPDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert(pvpEntry: PvPEntry)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertAll(pvpEntries: List<PvPEntry>)

	@Query("SELECT * FROM pvp_entries")
	fun getAll(): List<PvPEntry>

	@Query("SELECT * FROM pvp_entries WHERE aKey=:aKey")
	fun getByKey(aKey: Int): PvPEntry

	@Query("SELECT * FROM pvp_entries WHERE region=:region AND pvp_region_id=:pvpRegion AND season_id=:season")
	fun getByData(region: String, pvpRegion: Int, season: Int): PvPEntry

	@Query("SELECT season_id FROM pvp_entries WHERE region=:region AND pvp_region_id=:pvpRegionId ORDER BY season_id ASC")
	fun getSeasonsByRegionId(region: String, pvpRegionId: Int): List<Int>
}