package self.edu.kurtis.placebook.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.content.Context
import android.graphics.Bitmap
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import self.edu.kurtis.placebook.model.Bookmark
import self.edu.kurtis.placebook.repository.BookmarkRepo
import self.edu.kurtis.placebook.util.ImageUtils

class BookmarkDetailsViewModel(application: Application) : AndroidViewModel(application) {
    private var bookmarkRepo: BookmarkRepo = BookmarkRepo(getApplication())
    private var bookmarkDetailsView: LiveData<BookmarkDetailsView>? = null

    data class BookmarkDetailsView(
            var id: Long? = null,
            var name: String = "",
            var phone: String = "",
            var address: String = "",
            var notes: String = "",
            var category: String = ""
    ) {
        fun getImage(context: Context): Bitmap? {
            id?.let {
                return ImageUtils.loadBitmapFromFile(context, Bookmark.generateImageFilename(it))
            }
            return null
        }

        fun setImage(context: Context, image: Bitmap) {
            id?.let {
                ImageUtils.saveBitmapToFile(context, image, Bookmark.generateImageFilename(it))
            }
        }
    }

    private fun bookmarkToBookmarkView(bookmark: Bookmark) : BookmarkDetailsView {
        return BookmarkDetailsView(
                bookmark.id,
                bookmark.name,
                bookmark.phone,
                bookmark.address,
                bookmark.notes,
                bookmark.category
        )
    }

    private fun mapBookmarkToBookmarkView(bookmarkId: Long) {
        val bookmark = bookmarkRepo.getLiveBookmark(bookmarkId)
        bookmarkDetailsView = Transformations.map(bookmark) {bookmark ->
            bookmark?.let {
                val bookmarkView = bookmarkToBookmarkView(bookmark)
                bookmarkView
            }
        }
    }

    fun getBookmark(bookmarkId: Long): LiveData<BookmarkDetailsView>? {
        if (bookmarkDetailsView == null) {
            mapBookmarkToBookmarkView(bookmarkId)
        }
        return bookmarkDetailsView
    }

    private fun bookmarkViewToBookmark(bookmarkView: BookmarkDetailsView): Bookmark? {
        val bookmark = bookmarkView.id?.let {
            bookmarkRepo.getBookmark(it)
        }
        if (bookmark != null) {
            bookmark.id = bookmarkView.id
            bookmark.name = bookmarkView.name
            bookmark.phone = bookmarkView.phone
            bookmark.address = bookmarkView.address
            bookmark.notes = bookmarkView.notes
            bookmark.category = bookmarkView.category
        }
        return bookmark
    }

    fun updateBookmark(bookmarkView: BookmarkDetailsView) {
        launch(CommonPool) {
            val bookmark = bookmarkViewToBookmark(bookmarkView)
            bookmark?.let { bookmarkRepo.updateBookmark(it) }
        }
    }

    fun getCategoryResourceId(category: String) : Int? {
        return bookmarkRepo.getCategoryResourceId(category)
    }

    fun getCategories() : List<String> {
        return bookmarkRepo.categories
    }

    fun deleteBookmark(bookmarkDetailsView: BookmarkDetailsView) {
        launch(CommonPool) {
            val bookmark = bookmarkDetailsView.id?.let {
                bookmarkRepo.getBookmark(it)
            }
            bookmark?.let {
                bookmarkRepo.deleteBookmark(it)
            }
        }
    }
}
