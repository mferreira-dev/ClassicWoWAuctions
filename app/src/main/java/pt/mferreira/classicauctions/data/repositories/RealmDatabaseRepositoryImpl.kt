package pt.mferreira.classicauctions.data.repositories

import android.app.Application
import pt.mferreira.classicauctions.data.databases.AppDatabase
import pt.mferreira.classicauctions.data.entities.Realm
import pt.mferreira.classicauctions.domain.repositories.RealmDatabaseRepository

class RealmDatabaseRepositoryImpl(application: Application) : RealmDatabaseRepository {
	private val realmsDao = AppDatabase.getInstance(application).realmsDao()

	override suspend fun insert(realm: Realm) {
		realmsDao.insert(realm)
	}

	override suspend fun insertAll(realms: List<Realm>) {
		realmsDao.insertAll(realms)
	}

	override suspend fun getAll(): List<Realm> {
		return realmsDao.getAll()
	}

	override suspend fun getAllDistinct(): List<Realm> {
		return realmsDao.getAllDistinct()
	}

	override suspend fun getById(id: String): Realm {
		return realmsDao.getById(id)
	}

	override suspend fun getEraClusterIdByName(name: String, eraString: String): Int {
		return realmsDao.getEraClusterIdByName(name, eraString)
	}

	override suspend fun getRussianEraClusterIdByName(name: String): List<Int> {
		return realmsDao.getRussianEraClusterIdByName(name)
	}

	companion object {
		@Volatile
		private var instance: RealmDatabaseRepositoryImpl? = null

		@JvmStatic
		fun getInstance(application: Application) =
			instance ?: synchronized(this) {
				instance ?: RealmDatabaseRepositoryImpl(application).also { instance = it }
			}
	}
}