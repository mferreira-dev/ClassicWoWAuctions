package pt.mferreira.classicauctions.utils

sealed class Failure {
	object BadRequest : Failure()
	object Unauthorized : Failure()
	object Forbidden : Failure()
	object NotFound : Failure()
	object Other : Failure()
	object NotConnected : Failure()
	object ServerError : Failure()
}