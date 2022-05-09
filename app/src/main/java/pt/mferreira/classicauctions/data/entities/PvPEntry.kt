package pt.mferreira.classicauctions.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import pt.mferreira.classicauctions.utils.extensions.empty

@Entity(tableName = "pvp_entries")
data class PvPEntry(
	@PrimaryKey(autoGenerate = true) var aKey: Int = 0,
	@ColumnInfo(name = "region") var region: String = String.empty(),
	@ColumnInfo(name = "pvp_region_id") var pvpRegionId: Int = -1,
	@ColumnInfo(name = "season_id") var seasonId: Int = -1,
	@ColumnInfo(name = "brackets_json") var bracketsJson: String = String.empty()
)