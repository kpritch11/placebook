package self.edu.kurtis.placebook.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.graphics.Bitmap
import android.util.Log
import com.google.android.gms.location.places.Place
import self.edu.kurtis.placebook.repository.BookmarkRepo

class MapsViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "MapsViewModel"
    private var bookmarkRepo: BookmarkRepo = BookmarkRepo(getApplication())

    fun addBookmarkFromPlace(place: Place, image: Bitmap) {
        val bookmark = bookmarkRepo.createBookmark()
        bookmark.placeId = place.id
        bookmark.name = place.name.toString()
        bookmark.longitude = place.latLng.longitude
        bookmark.latitude = place.latLng.latitude
        bookmark.phone = place.phoneNumber.toString()
        bookmark.address = place.address.toString()

        val newId = bookmarkRepo.addBookmark(bookmark)
        Log.i(TAG, "New bookmark $newId added to the database.")
    }
}