package pt.mferreira.classicauctions.domain.repositories

import pt.mferreira.classicauctions.data.models.ClassIndexResponse
import pt.mferreira.classicauctions.data.models.ClassMediaResponse

interface ClassHttpRepository {
	suspend fun getClassIndex(): ClassIndexResponse
	suspend fun getClassMedia(classId: Int): ClassMediaResponse
}