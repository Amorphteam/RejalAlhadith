package org.masaha.rejalalhadith.ui.main

import org.masaha.rejalalhadith.database.Bookmark
import org.masaha.rejalalhadith.database.RejalGhavaed
import org.masaha.rejalalhadith.database.RejalLink

interface OnTabItemClickListener {
    fun onItemClicked(rejal: RejalLink, filter: String)
    fun onItemClicked(ghavaed: RejalGhavaed)
    fun onItemClicked(bookmark: Bookmark)
    fun onDeleteBookmark(bookmark: Bookmark)
}