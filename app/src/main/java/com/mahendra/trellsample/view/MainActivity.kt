package com.mahendra.trellsample.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mahendra.trellsample.R
import com.mahendra.trellsample.view.videolist.VideoListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadListFragment();
    }

    private fun loadListFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.rootView,VideoListFragment.newInstance(),VideoListFragment::class.java.name)
            .commit();
    }

}
