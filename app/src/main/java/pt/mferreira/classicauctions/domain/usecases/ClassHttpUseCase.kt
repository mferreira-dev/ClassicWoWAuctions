package pt.mferreira.classicauctions.domain.usecases

import pt.mferreira.classicauctions.data.models.ClassIndexResponse
import pt.mferreira.classicauctions.data.models.ClassMediaResponse
import pt.mferreira.classicauctions.domain.repositories.ClassHttpRepository
import pt.mferreira.classicauctions.utils.Either
import pt.mferreira.classicauctions.utils.Failure
import pt.mferreira.classicauctions.utils.NoConnectivityException
import retrofit2.HttpException

class ClassHttpUseCase(private val repository: ClassHttpRepository) {
	suspend fun getClassIndex(): Either<Failure, ClassIndexResponse> {
		return try {
			val response = repository.getClassIndex()
			Either.Success(response)
		} catch (ex: NoConnectivityException) {
			Either.Failure(Failure.NotConnected)
		} catch (ex: HttpException) {
			Either.Failure(Failure.ServerError)
		} catch (ex: Exception) {
			Either.Failure(Failure.Other)
		}
	}

	suspend fun getClassMedia(classId: Int): Either<Failure, ClassMediaResponse> {
		return try {
			val response = repository.getClassMedia(classId)
			Either.Success(response)
		} catch (ex: NoConnectivityException) {
			Either.Failure(Failure.NotConnected)
		} catch (ex: HttpException) {
			Either.Failure(Failure.ServerError)
		} catch (ex: Exception) {
			Either.Failure(Failure.Other)
		}
	}
}