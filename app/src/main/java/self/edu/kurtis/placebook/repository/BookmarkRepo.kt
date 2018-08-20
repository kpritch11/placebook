package self.edu.kurtis.placebook.repository

import android.arch.lifecycle.LiveData
import android.content.Context
import com.google.android.gms.location.places.Place
import self.edu.kurtis.placebook.R
import self.edu.kurtis.placebook.db.BookmarkDao
import self.edu.kurtis.placebook.db.PlaceBookDatabase
import self.edu.kurtis.placebook.model.Bookmark

class BookmarkRepo(private val context: Context) {
    private var db: PlaceBookDatabase = PlaceBookDatabase.getInstance(context)
    private var bookmarkDao: BookmarkDao = db.bookmarkDao()
    private var categoryMap: HashMap<Int, String> = buildCategoryMap()
    private var allCategories: HashMap<String, Int> = buildCategories()
    val categories: List<String>
        get() = ArrayList(allCategories.keys)

    fun addBookmark(bookmark: Bookmark): Long? {
        val newId = bookmarkDao.insertBookmark(bookmark)
        bookmark.id = newId
        return newId
    }

    fun createBookmark(): Bookmark {
        return Bookmark()
    }

    val allBookmarks: LiveData<List<Bookmark>>
        get() {
            return bookmarkDao.loadAll()
        }

    fun getLiveBookmark(bookmarkId: Long) : LiveData<Bookmark> {
        val bookmark = bookmarkDao.loadLiveBookmark(bookmarkId)
        return bookmark
    }

    fun updateBookmark(bookmark: Bookmark) {
        bookmarkDao.updateBookmark(bookmark)
    }

    fun getBookmark(bookmarkId: Long): Bookmark {
        return bookmarkDao.loadBookmark(bookmarkId)
    }

    private fun buildCategoryMap() : HashMap<Int, String> {
        return hashMapOf(
                Place.TYPE_BAKERY to "Restaurant",
                Place.TYPE_BAR to "Restaurant",
                Place.TYPE_CAFE to "Restaurant",
                Place.TYPE_FOOD to "Restaurant",
                Place.TYPE_RESTAURANT to "Restaurant",
                Place.TYPE_MEAL_DELIVERY to "Restaurant",
                Place.TYPE_MEAL_TAKEAWAY to "Restaurant",
                Place.TYPE_GAS_STATION to "Gas",
                Place.TYPE_CLOTHING_STORE to "Shopping",
                Place.TYPE_DEPARTMENT_STORE to "Shopping",
                Place.TYPE_FURNITURE_STORE to "Shopping",
                Place.TYPE_GROCERY_OR_SUPERMARKET to "Shopping",
                Place.TYPE_HARDWARE_STORE to "Shopping",
                Place.TYPE_HOME_GOODS_STORE to "Shopping",
                Place.TYPE_JEWELRY_STORE to "Shopping",
                Place.TYPE_SHOE_STORE to "Shopping",
                Place.TYPE_SHOPPING_MALL to "Shopping",
                Place.TYPE_STORE to "Shopping",
                Place.TYPE_LODGING to "Lodging",
                Place.TYPE_ROOM to "Lodging"
        )
    }

    fun placeTypeToCategory(placeType: Int) : String {
        var category = "Other"
        if (categoryMap.containsKey(placeType)) {
            category = categoryMap[placeType].toString()
        }
        return category
    }

    private fun buildCategories() : HashMap<String, Int> {
        return hashMapOf(
                "Gas" to R.drawable.ic_gas,
                "Lodging" to R.drawable.ic_lodging,
                "Other" to R.drawable.ic_other,
                "Restaurant" to R.drawable.ic_restaurant,
                "Shopping" to R.drawable.ic_shopping
        )
    }

    fun getCategoryResourceId(placeCategory: String) : Int? {
        return allCategories[placeCategory]
    }

    fun deleteBookmark(bookmark: Bookmark) {
        bookmark.deleteImage(context)
        bookmarkDao.deleteBookmark(bookmark)
    }
}