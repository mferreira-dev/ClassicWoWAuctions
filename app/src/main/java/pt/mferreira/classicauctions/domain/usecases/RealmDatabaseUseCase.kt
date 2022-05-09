package pt.mferreira.classicauctions.domain.usecases

import pt.mferreira.classicauctions.data.entities.Realm
import pt.mferreira.classicauctions.domain.repositories.RealmDatabaseRepository

class RealmDatabaseUseCase(private val repository: RealmDatabaseRepository) {
	suspend fun insert(realm: Realm) = repository.insert(realm)
	suspend fun insertAll(realms: List<Realm>) = repository.insertAll(realms)
	suspend fun getAll(): List<Realm> = repository.getAll()
	suspend fun getAllDistinct(): List<Realm> = repository.getAllDistinct()
	suspend fun getById(id: String): Realm = repository.getById(id)
	suspend fun getEraClusterIdByName(name: String, eraString: String): Int =
		repository.getEraClusterIdByName(name, eraString)

	suspend fun getRussianEraClusterIdByName(name: String): List<Int> =
		repository.getRussianEraClusterIdByName(name)
}