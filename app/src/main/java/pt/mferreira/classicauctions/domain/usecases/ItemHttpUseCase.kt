package pt.mferreira.classicauctions.domain.usecases

import pt.mferreira.classicauctions.data.models.ItemCategoriesResponse
import pt.mferreira.classicauctions.data.models.ItemMediaResponse
import pt.mferreira.classicauctions.data.models.ItemSearchResponse
import pt.mferreira.classicauctions.data.models.ItemSubCategoriesResponse
import pt.mferreira.classicauctions.domain.repositories.ItemHttpRepository
import pt.mferreira.classicauctions.utils.Either
import pt.mferreira.classicauctions.utils.Failure
import pt.mferreira.classicauctions.utils.NoConnectivityException
import retrofit2.HttpException

class ItemHttpUseCase(private val repository: ItemHttpRepository) {
	suspend fun getItemCategories(): Either<Failure, ItemCategoriesResponse> {
		return try {
			val response = repository.getItemCategories()
			Either.Success(response)
		} catch (ex: NoConnectivityException) {
			Either.Failure(Failure.NotConnected)
		} catch (ex: HttpException) {
			Either.Failure(Failure.ServerError)
		} catch (ex: Exception) {
			Either.Failure(Failure.Other)
		}
	}

	suspend fun getItemSubCategories(categoryId: Int): Either<Failure, ItemSubCategoriesResponse> {
		return try {
			val response = repository.getItemSubCategories(categoryId)
			Either.Success(response)
		} catch (ex: NoConnectivityException) {
			Either.Failure(Failure.NotConnected)
		} catch (ex: HttpException) {
			Either.Failure(Failure.ServerError)
		} catch (ex: Exception) {
			Either.Failure(Failure.Other)
		}
	}

	suspend fun itemSearch(keyword: String): Either<Failure, ItemSearchResponse> {
		return try {
			val response = repository.itemSearch(keyword)
			Either.Success(response)
		} catch (ex: NoConnectivityException) {
			Either.Failure(Failure.NotConnected)
		} catch (ex: HttpException) {
			Either.Failure(Failure.ServerError)
		} catch (ex: Exception) {
			Either.Failure(Failure.Other)
		}
	}

	suspend fun getItemMedia(itemId: Int): Either<Failure, ItemMediaResponse> {
		return try {
			val response = repository.getItemMedia(itemId)
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