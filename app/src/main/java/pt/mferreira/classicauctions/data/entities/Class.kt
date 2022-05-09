package pt.mferreira.classicauctions.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import pt.mferreira.classicauctions.utils.extensions.empty

@Entity(tableName = "classes")
data class Class(
	@PrimaryKey(autoGenerate = true) var key: Int = 0,
	@ColumnInfo(name = "id") var id: Int = 0,
	@ColumnInfo(name = "name") var name: String = String.empty(),
	@ColumnInfo(name = "media_url") var icon: String = String.empty()
)