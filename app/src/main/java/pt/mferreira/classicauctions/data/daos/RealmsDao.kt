package pt.mferreira.classicauctions.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pt.mferreira.classicauctions.data.entities.Realm

@Dao
interface RealmsDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert(realm: Realm)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertAll(realms: List<Realm>)

	@Query("SELECT * FROM realms")
	fun getAll(): List<Realm>

	@Query("SELECT DISTINCT * FROM realms GROUP BY name")
	fun getAllDistinct(): List<Realm>

	@Query("SELECT * FROM realms WHERE id=:id")
	fun getById(id: String): Realm

	@Query("SELECT clusterId FROM realms WHERE name=:name AND category=:eraString")
	fun getEraClusterIdByName(name: String, eraString: String): Int

	/**
	 * Russian era servers are identified as "Russian" instead of "Classic Era".
	 * */
	@Query("SELECT clusterId FROM realms WHERE name=:name")
	fun getRussianEraClusterIdByName(name: String): List<Int>
}