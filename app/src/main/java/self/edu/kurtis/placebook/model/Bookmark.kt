package self.edu.kurtis.placebook.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.content.Context
import android.graphics.Bitmap
import self.edu.kurtis.placebook.util.FileUtils
import self.edu.kurtis.placebook.util.ImageUtils

@Entity
data class Bookmark(
        @PrimaryKey(autoGenerate = true) var id: Long? = null,
        var placeId: String? = null,
        var name: String = "",
        var address: String = "",
        var latitude: Double = 0.0,
        var longitude: Double = 0.0,
        var phone: String = "",
        var notes: String = "",
        var category: String = ""
) {
    fun setImage(image: Bitmap, context: Context) {
        id?.let {
            ImageUtils.saveBitmapToFile(context, image, generateImageFilename(it))
        }
    }

    companion object {
        fun generateImageFilename(id: Long): String {
            return "bookmark$id.png"
        }
    }

    fun deleteImage(context: Context) {
        id?.let {
            FileUtils.deleteFile(context, generateImageFilename(it))
        }
    }
}