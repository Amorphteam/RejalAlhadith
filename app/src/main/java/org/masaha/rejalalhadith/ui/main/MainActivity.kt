package org.masaha.rejalalhadith.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDelegate
import org.masaha.rejalalhadith.ui.BaseAppCompatActivity
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
import org.masaha.rejalalhadith.utils.PrefManager
import org.masaha.rejalalhadith.utils.SearchMode
import org.masaha.rejalalhadith.utils.ThemeMode
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : BaseAppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener, FloatingSearchView.OnMenuItemClickListener,
        FloatingSearchView.OnQueryChangeListener, OnTabItemClickListener {

    var firstFragment: RejalFragment? = null
    var secondFragment: GhavaedFragment? = null
    var thirdFragment: BookmarkFragment? = null

    private val subscriptions: CompositeDisposable = CompositeDisposable()
    private lateinit var prefManager: PrefManager
    private var searchMode: SearchMode = SearchMode.NAME_CONTAINS
    private var currentSearchQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefManager = PrefManager(this)
        searchMode = prefManager.getSearchMode()

        searchView.attachNavigationDrawerToMenuButton(drawer_layout)
        searchView.setOnMenuItemClickListener(this)
        searchView.setOnQueryChangeListener(this)
        tabMenuButton.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }
        nav_view.setNavigationItemSelectedListener(this)

        //set bottom nav click listener
        bottomNavigation.setOnNavigationItemSelectedListener(this)

        setupSearchModeSelector()

        // load first fragment
        firstFragment = RejalFragment.newInstance()
        secondFragment = GhavaedFragment.newInstance()
        thirdFragment = BookmarkFragment.newInstance()

        replaceFragment(firstFragment!!)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        applySearchViewTheme()
        searchView.post { tightenSearchBarSpacing() }
    }

    private fun applySearchViewTheme() {
        val surface = resources.getColor(R.color.surface)
        val textPrimary = resources.getColor(R.color.text_primary)
        val textSecondary = resources.getColor(R.color.text_secondary)

        searchView.setBackgroundColor(surface)
        searchView.setViewTextColor(textPrimary)
        searchView.setQueryTextColor(textPrimary)
        searchView.setHintTextColor(textSecondary)
        searchView.setLeftActionIconColor(textSecondary)
        searchView.setClearBtnColor(textSecondary)
        searchView.setMenuItemIconColor(textSecondary)
        searchView.setActionMenuOverflowColor(textSecondary)
        searchView.setDividerColor(resources.getColor(R.color.page_background))
    }

    private fun tightenSearchBarSpacing() {
        val gap = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                4f,
                resources.displayMetrics
        ).toInt()

        val inputParentId = resources.getIdentifier("search_input_parent", "id", packageName)
        if (inputParentId != 0) {
            val inputParent = searchView.findViewById<View>(inputParentId)
            val params = inputParent?.layoutParams as? ViewGroup.MarginLayoutParams
            if (params != null) {
                params.marginStart = gap
                params.leftMargin = gap
                inputParent.layoutParams = params
            }
        }

        val leftContainerId = resources.getIdentifier(
                "search_bar_left_action_container",
                "id",
                packageName
        )
        if (leftContainerId != 0) {
            val leftContainer = searchView.findViewById<View>(leftContainerId)
            val params = leftContainer?.layoutParams as? ViewGroup.MarginLayoutParams
            if (params != null) {
                params.marginStart = gap
                params.leftMargin = gap
                leftContainer.layoutParams = params
            }
        }

        val inputId = resources.getIdentifier("search_bar_text", "id", packageName)
        if (inputId != 0) {
            val input = searchView.findViewById<EditText>(inputId) ?: return
            input.gravity = Gravity.START or Gravity.CENTER_VERTICAL
            input.textDirection = View.TEXT_DIRECTION_RTL
            input.setPaddingRelative(gap, input.paddingTop, gap, input.paddingBottom)
        }
    }

    private fun setupSearchModeSelector() {
        when (searchMode) {
            SearchMode.NAME_STARTS_WITH -> searchModeStartsWith.isChecked = true
            SearchMode.NAME_CONTAINS -> searchModeContains.isChecked = true
            SearchMode.DESCRIPTION -> searchModeDescription.isChecked = true
        }

        searchModeGroup.setOnCheckedChangeListener { _, checkedId ->
            searchMode = when (checkedId) {
                R.id.searchModeStartsWith -> SearchMode.NAME_STARTS_WITH
                R.id.searchModeDescription -> SearchMode.DESCRIPTION
                else -> SearchMode.NAME_CONTAINS
            }
            prefManager.saveSearchMode(searchMode)
            firstFragment?.searchRejals(currentSearchQuery, searchMode)
        }
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
            R.id.nav_theme -> {
                showThemeDialog()
            }

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

            R.id.allapps->{
                val webIntent = Intent(Intent.ACTION_VIEW)
                val data = Uri.parse("https://play.google.com/store/apps/dev?id=8323529867410855923")
                webIntent.data = data
                startActivity(webIntent)
            }
            R.id.bottom_bar_tab1 -> {
                showMainTab()
                replaceFragment(firstFragment!!)
            }
            R.id.bottom_bar_tab2 -> {
                showTabTitle(R.string.tab_ghavaed)
                replaceFragment(secondFragment!!)
            }
            R.id.bottom_bar_tab3 -> {
                showTabTitle(R.string.tab_bookmarks)
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

    private fun showMainTab() {
        searchView.visibility = View.VISIBLE
        searchModeGroup.visibility = View.VISIBLE
        tabTitleBar.visibility = View.GONE
    }

    private fun showTabTitle(titleRes: Int) {
        searchView.visibility = View.GONE
        searchModeGroup.visibility = View.GONE
        tabTitle.text = getString(titleRes)
        tabTitleBar.visibility = View.VISIBLE
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onSearchTextChanged(oldQuery: String?, newQuery: String?) {
        currentSearchQuery = newQuery.orEmpty()
        firstFragment?.searchRejals(currentSearchQuery, searchMode)
    }

    override fun onItemClicked(rejal: RejalLink, filter: String, searchMode: SearchMode) {
        val bundle = Bundle()
        bundle.putParcelable(Constants.EXTRA_REJAL_LINK, rejal)
        bundle.putString(Constants.EXTRA_REJAL_FILTER, filter)
        bundle.putSerializable(Constants.EXTRA_SEARCH_MODE, searchMode)
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

    private fun showThemeDialog() {
        val options = arrayOf(
                getString(R.string.theme_light),
                getString(R.string.theme_dark),
                getString(R.string.theme_system)
        )
        val currentMode = prefManager.getThemeMode()

        val dialog = AlertDialog.Builder(this)
                .setTitle(R.string.theme)
                .setSingleChoiceItems(options, currentMode.ordinal) { dialogInterface, which ->
                    val selected = ThemeMode.fromOrdinal(which)
                    dialogInterface.dismiss()
                    if (selected != currentMode) {
                        applyThemeMode(selected)
                    }
                }
                .create()
        dialog.show()
        applyRtl(dialog.window?.decorView)
        applyRtl(dialog.listView)
    }

    private fun applyRtl(view: View?) {
        view?.layoutDirection = View.LAYOUT_DIRECTION_RTL
        view?.textDirection = View.TEXT_DIRECTION_RTL
        if (view is ViewGroup) {
            for (index in 0 until view.childCount) {
                applyRtl(view.getChildAt(index))
            }
        }
    }

    private fun applyThemeMode(mode: ThemeMode) {
        prefManager.saveThemeMode(mode)
        val nightMode = mode.toNightMode()
        AppCompatDelegate.setDefaultNightMode(nightMode)
        delegate.setLocalNightMode(nightMode)
        recreate()
    }

    fun startTextViewer(bundle: Bundle) {
        val intent = Intent(this, TextViewer::class.java)
        intent.replaceExtras(bundle)
        startActivity(intent)
    }
}
