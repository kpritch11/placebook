package self.edu.kurtis.placebook.repository

import android.arch.lifecycle.LiveData
import android.content.Context
import self.edu.kurtis.placebook.db.BookmarkDao
import self.edu.kurtis.placebook.db.PlaceBookDatabase
import self.edu.kurtis.placebook.model.Bookmark

class BookmarkRepo(private val context: Context) {
    private var db: PlaceBookDatabase = PlaceBookDatabase.getInstance(context)
    private var bookmarkDao: BookmarkDao = db.bookmarkDao()

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
}