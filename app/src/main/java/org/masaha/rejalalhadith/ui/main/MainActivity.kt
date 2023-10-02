package org.masaha.rejalalhadith.ui.main

import android.content.Intent
import android.net.Uri
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
import org.masaha.rejalalhadith.database.Bookmark
import org.masaha.rejalalhadith.database.DataRepositoryImpl
import org.masaha.rejalalhadith.database.RejalGhavaed
import org.masaha.rejalalhadith.database.RejalLink
import org.masaha.rejalalhadith.ui.main.tabbookmark.BookmarkFragment
import org.masaha.rejalalhadith.ui.main.tabghavaed.GhavaedFragment
import org.masaha.rejalalhadith.ui.main.tabrejal.RejalFragment
import org.masaha.rejalalhadith.ui.viewer.TextViewer
import org.masaha.rejalalhadith.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.widget.Toast


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener, FloatingSearchView.OnMenuItemClickListener,
        FloatingSearchView.OnQueryChangeListener, OnTabItemClickListener {

    var firstFragment: RejalFragment? = null
    var secondFragment: GhavaedFragment? = null
    var thirdFragment: BookmarkFragment? = null

    private val subscriptions: CompositeDisposable = CompositeDisposable()

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
            finish()
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
            R.id.nav_about -> {

                val aboutUs = Intent (this, About::class.java)

                startActivity(aboutUs)

            }

            R.id.nav_share -> {
                try {
                    val i = Intent(Intent.ACTION_SEND)
                    i.type = "text/plain"
                    i.putExtra(Intent.EXTRA_SUBJECT, "رجال الحديث")
                    var sAux = "\nتحميل تطبيق رجال الحديث لنظام الاندرويد\n\n"
                    sAux += "https://play.google.com/store/apps/details?id=org.masaha.rejalalhadith\n\n"
                    i.putExtra(Intent.EXTRA_TEXT, sAux)
                    startActivity(Intent.createChooser(i, "اختر واحداً"))
                } catch (e: Exception) {
                    //e.toString();
                }
            }

            R.id.nav_contact -> {
                val mailIntent = Intent(Intent.ACTION_VIEW)
                val data = Uri.parse("mailto:?subject=" + "مرسل من تطبيق رجال الحديث" + "&body=" + "أدخل نصاً" + "&to=" + "info@masaha.org")
                mailIntent.data = data
                startActivity(Intent.createChooser(mailIntent, "أرسل بريداً"))


            }

            R.id.error_contact->{
                val mailIntent = Intent(Intent.ACTION_VIEW)
                val data = Uri.parse("mailto:?subject=" + "مرسل من تطبيق رجال الحديث" + "&body=" + "أدخل نصاً" + "&to=" + "err@masaha.org")
                mailIntent.data = data
                startActivity(Intent.createChooser(mailIntent, "الإبلاغ عن خطأ"))

            }

            R.id.allapps->{
                val webIntent = Intent(Intent.ACTION_VIEW)
                val data = Uri.parse("https://play.google.com/store/apps/dev?id=8323529867410855923")
                webIntent.data = data
                startActivity(webIntent)
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

        subscriptions.clear()
        subscriptions.dispose()

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

    override fun onDeleteBookmark(bookmark: Bookmark) {
        subscriptions.add(
                DataRepositoryImpl.getInstance().deleteBookmark(bookmark.bookmarkId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            // nop
                        }, { e ->
                            e.printStackTrace()
                        })
        )
    }

    fun startTextViewer(bundle: Bundle) {
        val intent = Intent(this, TextViewer::class.java)
        intent.replaceExtras(bundle)
        startActivity(intent)
    }
}
