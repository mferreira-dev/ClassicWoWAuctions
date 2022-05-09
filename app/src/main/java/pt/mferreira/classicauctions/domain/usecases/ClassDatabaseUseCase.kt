package pt.mferreira.classicauctions.domain.usecases

import pt.mferreira.classicauctions.data.entities.Class
import pt.mferreira.classicauctions.domain.repositories.ClassDatabaseRepository

class ClassDatabaseUseCase(private val repository: ClassDatabaseRepository) {
	suspend fun insert(aClass: Class) = repository.insert(aClass)
	suspend fun insertAll(classes: List<Class>) = repository.insertAll(classes)
	suspend fun getAll(): List<Class> = repository.getAll()
	suspend fun getById(id: String): Class = repository.getById(id)
}