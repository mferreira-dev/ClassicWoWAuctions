package pt.mferreira.classicauctions.domain.repositories

import pt.mferreira.classicauctions.data.entities.Class

interface ClassDatabaseRepository {
	suspend fun insert(aClass: Class)
	suspend fun insertAll(classes: List<Class>)
	suspend fun getAll(): List<Class>
	suspend fun getById(id: String): Class
}