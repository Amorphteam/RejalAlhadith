package com.papyrus.mehdok.rejalalhadith.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.arlib.floatingsearchview.FloatingSearchView
import com.papyrus.mehdok.rejalalhadith.R
import com.papyrus.mehdok.rejalalhadith.database.Bookmark
import com.papyrus.mehdok.rejalalhadith.database.RejalGhavaed
import com.papyrus.mehdok.rejalalhadith.database.RejalLink
import com.papyrus.mehdok.rejalalhadith.ui.main.tabbookmark.BookmarkFragment
import com.papyrus.mehdok.rejalalhadith.ui.main.tabghavaed.GhavaedFragment
import com.papyrus.mehdok.rejalalhadith.ui.main.tabrejal.RejalFragment
import com.papyrus.mehdok.rejalalhadith.ui.viewer.TextViewer
import com.papyrus.mehdok.rejalalhadith.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener, FloatingSearchView.OnMenuItemClickListener,
        FloatingSearchView.OnQueryChangeListener, OnTabItemClickListener {

    var firstFragment: RejalFragment? = null
    var secondFragment: GhavaedFragment? = null
    var thirdFragment: BookmarkFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchView.attachNavigationDrawerToMenuButton(drawer_layout)
        searchView.setOnMenuItemClickListener(this)
        searchView.setOnQueryChangeListener(this)

        nav_view.setNavigationItemSelectedListener(this)

        //set bottom nav click listener
        bottomNavigation.setOnNavigationItemSelectedListener(this)

        // load first fragment
        firstFragment = RejalFragment.newInstance()
        secondFragment = GhavaedFragment.newInstance()
        thirdFragment = BookmarkFragment.newInstance()

        replaceFragment(firstFragment!!)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onActionMenuItemSelected(item: MenuItem?) {
        when (item?.itemId) {
            R.id.action_settings -> {
                Log.e("MainActivity", "action_settings")
            }
            else -> {
                Log.e("MainActivity", "nop")
            }
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
            R.id.bottom_bar_tab1 -> {
                searchView.visibility = View.VISIBLE
                replaceFragment(firstFragment!!)
            }
            R.id.bottom_bar_tab2 -> {
                searchView.visibility = View.INVISIBLE
                replaceFragment(secondFragment!!)
            }
            R.id.bottom_bar_tab3 -> {
                searchView.visibility = View.INVISIBLE
                replaceFragment(thirdFragment!!)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        firstFragment = null
        secondFragment = null
        thirdFragment = null
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onSearchTextChanged(oldQuery: String?, newQuery: String?) {
        var query = ""
        if (newQuery != null) {
            query = newQuery
        }

        firstFragment?.searchRejals(query)
    }

    override fun onItemClicked(rejal: RejalLink, filter: String) {
        val bundle = Bundle()
        bundle.putParcelable(Constants.EXTRA_REJAL_LINK, rejal)
        bundle.putString(Constants.EXTRA_REJAL_FILTER, filter)
        bundle.putSerializable(Constants.EXTRA_VIEWER_TYPE, TextViewer.ViewerType.Rejal)
        startTextViewer(bundle)
    }

    override fun onItemClicked(ghavaed: RejalGhavaed) {
        val bundle = Bundle()
        bundle.putParcelable(Constants.EXTRA_GHAVAED_LINK, ghavaed)
        bundle.putSerializable(Constants.EXTRA_VIEWER_TYPE, TextViewer.ViewerType.Ghavaed)
        startTextViewer(bundle)
    }

    override fun onItemClicked(bookmark: Bookmark) {
        val bundle = Bundle()
        bundle.putParcelable(Constants.EXTRA_BOOKMARK, bookmark)
        bundle.putSerializable(Constants.EXTRA_VIEWER_TYPE, TextViewer.ViewerType.Bookmark)
        startTextViewer(bundle)
    }

    fun startTextViewer(bundle: Bundle) {
        val intent = Intent(this, TextViewer::class.java)
        intent.replaceExtras(bundle)
        startActivity(intent)
    }
}
