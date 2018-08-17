package self.edu.kurtis.placebook.ui

import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_bookmark_details.*
import self.edu.kurtis.placebook.R

class BookmarkDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark_details)
        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
    }
}