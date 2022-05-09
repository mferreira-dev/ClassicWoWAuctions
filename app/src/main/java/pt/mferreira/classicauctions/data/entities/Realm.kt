package pt.mferreira.classicauctions.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import pt.mferreira.classicauctions.utils.extensions.empty

@Entity(tableName = "realms")
data class Realm(
	@PrimaryKey(autoGenerate = true) var key: Int = 0,
	@ColumnInfo(name = "category") var category: String = String.empty(),
	@ColumnInfo(name = "id") var id: Int = 0,
	@ColumnInfo(name = "clusterId") var clusterId: Int = 0,
	@ColumnInfo(name = "name") var name: String = String.empty(),
	@ColumnInfo(name = "region") var region: String = String.empty(),
	@ColumnInfo(name = "type") var type: String = String.empty()
)