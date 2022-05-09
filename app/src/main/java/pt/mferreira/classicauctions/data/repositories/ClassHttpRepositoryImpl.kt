package pt.mferreira.classicauctions.data.repositories

import pt.mferreira.classicauctions.ClassicAuctions
import pt.mferreira.classicauctions.R
import pt.mferreira.classicauctions.data.models.ClassIndexResponse
import pt.mferreira.classicauctions.data.models.ClassMediaResponse
import pt.mferreira.classicauctions.data.network.Endpoints
import pt.mferreira.classicauctions.domain.repositories.ClassHttpRepository

class ClassHttpRepositoryImpl(private val endpoints: Endpoints) : ClassHttpRepository {
	override suspend fun getClassIndex(): ClassIndexResponse {
		return endpoints.getClassIndex(
			ClassicAuctions.applicationContext().getString(R.string.static_prog_eu)
		)
	}

	override suspend fun getClassMedia(classId: Int): ClassMediaResponse {
		return endpoints.getClassMedia(
			classId,
			ClassicAuctions.applicationContext().getString(R.string.static_prog_eu)
		)
	}

	companion object {
		@Volatile
		private var instance: ClassHttpRepositoryImpl? = null

		@JvmStatic
		fun getInstance(endpoints: Endpoints) =
			instance ?: synchronized(this) {
				instance ?: ClassHttpRepositoryImpl(endpoints).also { instance = it }
			}
	}
}