package pt.mferreira.classicauctions.domain.repositories

import pt.mferreira.classicauctions.data.entities.Realm

interface RealmDatabaseRepository {
	suspend fun insert(realm: Realm)
	suspend fun insertAll(realms: List<Realm>)
	suspend fun getAll(): List<Realm>
	suspend fun getAllDistinct(): List<Realm>
	suspend fun getById(id: String): Realm
	suspend fun getEraClusterIdByName(name: String, eraString: String): Int
	suspend fun getRussianEraClusterIdByName(name: String): List<Int>
}