package pt.mferreira.classicauctions.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class LeaderboardDetailsResponse(
	val season: Season,
	val bracket: Bracket,
	val entries: List<Entry>
) {
	data class Season(
		val id: Int
	)

	data class Bracket(
		val id: Int,
		@SerializedName("type") val name: String
	)

	@Parcelize
	data class Entry(
		val faction: Faction,
		val rank: Int,
		val rating: Int,
		@SerializedName("season_match_statistics") val seasonMatchStatistics: SeasonMatchStatistics,
		val team: Team
	) : Parcelable {
		@Parcelize
		data class Faction(
			@SerializedName("type") val name: String
		) : Parcelable

		@Parcelize
		data class Team(
			val name: String,
			val realm: Realm,
			val members: List<Member>
		) : Parcelable {
			@Parcelize
			data class Member(
				val character: Character,
				@SerializedName("season_match_statistics") val seasonMatchStatistics: SeasonMatchStatistics,
				val rating: Int
			) : Parcelable {
				@Parcelize
				data class Character(
					val name: String,
					val id: Int,
					val realm: Realm,
					@SerializedName("playable_class") val aClass: Class,
					@SerializedName("playable_race") val race: Race,
				) : Parcelable {
					@Parcelize
					data class Class(
						val id: Int
					) : Parcelable

					@Parcelize
					data class Race(
						val id: Int
					) : Parcelable
				}
			}
		}
	}
}

@Parcelize
data class Realm(
	val id: Int,
	@SerializedName("slug") val name: String
) : Parcelable

@Parcelize
data class SeasonMatchStatistics(
	val played: Int,
	val won: Int,
	val lost: Int
) : Parcelable