package pt.mferreira.classicauctions.utils

sealed class Either<out L, out R> {
	data class Failure<out L>(val failureValue: L) : Either<L, Nothing>()
	data class Success<out R>(val successValue: R) : Either<Nothing, R>()

	fun either(failure: (L) -> Any, success: (R) -> Any): Any =
		when (this) {
			is Failure -> failure(failureValue)
			is Success -> success(successValue)
		}
}