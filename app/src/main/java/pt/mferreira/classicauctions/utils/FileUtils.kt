package pt.mferreira.classicauctions.utils

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import pt.mferreira.classicauctions.ClassicAuctions
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class FileUtils {
	companion object {
		fun read(file: File): JsonReader {
			return JsonReader(FileReader(file))
		}

		fun findAndRead(filename: String): JsonReader? {
			listFiles()?.forEach {
				if (it.name.contains(filename))
					return JsonReader(FileReader(it))
			}

			return null
		}

		/**
		 * Out of date auctions/leaderboards will be deleted if they exist.
		 * */
		fun write(file: File, obj: Any) {
			val newFilename = file.name.substring(file.name.indexOf("_") + 1)

			listFiles()?.forEach {
				if (it.name.contains(newFilename))
					it.delete()
			}

			FileWriter(file).use { writer ->
				Gson().toJson(obj, writer)
			}
		}

		fun listFiles(): Array<out File>? {
			return ClassicAuctions.applicationContext().filesDir.listFiles()
		}

		fun doesFileExist(file: File): Boolean {
			return file.exists()
		}

		fun doesAnyFileContain(filename: String): Boolean {
			listFiles()?.forEach {
				if (it.name.contains(filename))
					return true
			}

			return false
		}
	}
}