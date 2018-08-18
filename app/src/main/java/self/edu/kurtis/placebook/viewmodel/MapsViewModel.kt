package self.edu.kurtis.placebook.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.google.android.gms.location.places.Place
import com.google.android.gms.maps.model.LatLng
import self.edu.kurtis.placebook.model.Bookmark
import self.edu.kurtis.placebook.repository.BookmarkRepo
import self.edu.kurtis.placebook.util.ImageUtils

class MapsViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "MapsViewModel"
    private var bookmarkRepo: BookmarkRepo = BookmarkRepo(getApplication())
    private var bookmarks: LiveData<List<BookmarkView>>? = null

    fun addBookmarkFromPlace(place: Place, image: Bitmap) {
        val bookmark = bookmarkRepo.createBookmark()
        bookmark.placeId = place.id
        bookmark.name = place.name.toString()
        bookmark.longitude = place.latLng.longitude
        bookmark.latitude = place.latLng.latitude
        bookmark.phone = place.phoneNumber.toString()
        bookmark.address = place.address.toString()

        val newId = bookmarkRepo.addBookmark(bookmark)
        bookmark.setImage(image, getApplication())
        Log.i(TAG, "New bookmark $newId added to the database.")
    }

    private fun bookmarkToBookmarkView(bookmark: Bookmark): MapsViewModel.BookmarkView {
        return MapsViewModel.BookmarkView(
                bookmark.id,
                LatLng(bookmark.latitude, bookmark.longitude),
                bookmark.name,
                bookmark.phone)
    }

    private fun mapBookmarksToBookmarkView() {
        val allBookmarks = bookmarkRepo.allBookmarks
        bookmarks = Transformations.map(allBookmarks) { bookmarks ->
            val bookmarkViews = bookmarks.map { bookmark ->
                bookmarkToBookmarkView(bookmark)
            }
            bookmarkViews
        }
    }

    fun getBookmarkViews() : LiveData<List<BookmarkView>>? {
        if (bookmarks == null) {
            mapBookmarksToBookmarkView()
        }
        return bookmarks
    }

    data class BookmarkView(
            var id: Long? = null,
            var location: LatLng = LatLng(0.0, 0.0),
            var name: String = "",
            var phone: String = ""
    ) {
        fun getImage(context: Context) : Bitmap? {
            id?.let {
                return ImageUtils.loadBitmapFromFile(context, Bookmark.generateImageFilename(it))
            }
            return null
        }
    }
}