package self.edu.kurtis.placebook.adapter

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import self.edu.kurtis.placebook.R
import self.edu.kurtis.placebook.ui.MapsActivity
import self.edu.kurtis.placebook.viewmodel.MapsViewModel

class BookmarkInfoWindowAdapter(val context: Activity) : GoogleMap.InfoWindowAdapter {
    private val contents: View

    init {
        contents = context.layoutInflater.inflate(R.layout.content_bookmark_info, null)
    }

    override fun getInfoWindow(marker: Marker?): View? {
       // This function is required, but can return null if not replacing the entier info window
        return null
    }

    override fun getInfoContents(marker: Marker) : View? {
        val titleView = contents.findViewById<TextView>(R.id.title)
        titleView.text = marker.title ?: ""

        val phoneView = contents.findViewById<TextView>(R.id.phone)
        phoneView.text = marker.snippet ?: ""

        val imageView = contents.findViewById<ImageView>(R.id.photo)
        when (marker.tag) {
            is MapsActivity.PlaceInfo -> {
                imageView.setImageBitmap((marker.tag as MapsActivity.PlaceInfo).image)
            }
            is MapsViewModel.BookmarkView -> {
                var bookmarkView = marker.tag as MapsViewModel.BookmarkView
                imageView.setImageBitmap(bookmarkView.getImage(context))
            }
        }


        return contents
    }
}