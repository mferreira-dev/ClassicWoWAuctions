package pt.mferreira.classicauctions.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Used as a wrapper for database operations (Room only accepts primitive types, hence the need for a JSON string).
 * */
data class BracketsWrapper(
	val brackets: MutableList<Bracket>
) {
	@Parcelize
	data class Bracket(
		val id: Int,
		val name: String
	) : Parcelable
}