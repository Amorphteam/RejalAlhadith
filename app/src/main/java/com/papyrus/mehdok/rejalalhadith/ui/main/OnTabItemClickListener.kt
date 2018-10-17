package com.papyrus.mehdok.rejalalhadith.ui.main

import com.papyrus.mehdok.rejalalhadith.database.Bookmark
import com.papyrus.mehdok.rejalalhadith.database.RejalGhavaed
import com.papyrus.mehdok.rejalalhadith.database.RejalLink

interface OnTabItemClickListener {
    fun onItemClicked(rejal: RejalLink, filter: String)
    fun onItemClicked(ghavaed: RejalGhavaed)
    fun onItemClicked(bookmark: Bookmark)
    fun onDeleteBookmark(bookmark: Bookmark)
}