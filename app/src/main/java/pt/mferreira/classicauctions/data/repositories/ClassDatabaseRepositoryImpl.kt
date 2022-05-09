package pt.mferreira.classicauctions.data.repositories

import android.app.Application
import pt.mferreira.classicauctions.data.databases.AppDatabase
import pt.mferreira.classicauctions.data.entities.Class
import pt.mferreira.classicauctions.domain.repositories.ClassDatabaseRepository

class ClassDatabaseRepositoryImpl(application: Application) : ClassDatabaseRepository {
	private val classesDao = AppDatabase.getInstance(application).classesDao()

	override suspend fun insert(aClass: Class) {
		classesDao.insert(aClass)
	}

	override suspend fun insertAll(classes: List<Class>) {
		classesDao.insertAll(classes)
	}

	override suspend fun getAll(): List<Class> {
		return classesDao.getAll()
	}

	override suspend fun getById(id: String): Class {
		return classesDao.getById(id)
	}

	companion object {
		@Volatile
		private var instance: ClassDatabaseRepositoryImpl? = null

		@JvmStatic
		fun getInstance(application: Application) =
			instance ?: synchronized(this) {
				instance ?: ClassDatabaseRepositoryImpl(application).also { instance = it }
			}
	}
}