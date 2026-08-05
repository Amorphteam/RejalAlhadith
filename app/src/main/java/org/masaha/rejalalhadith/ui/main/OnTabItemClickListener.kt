package org.masaha.rejalalhadith.ui.main

import org.masaha.rejalalhadith.database.Bookmark
import org.masaha.rejalalhadith.database.RejalGhavaed
import org.masaha.rejalalhadith.database.RejalLink
import org.masaha.rejalalhadith.utils.SearchMode

interface OnTabItemClickListener {
    fun onItemClicked(rejal: RejalLink, filter: String, searchMode: SearchMode)
    fun onItemClicked(ghavaed: RejalGhavaed)
    fun onItemClicked(bookmark: Bookmark)
    fun onDeleteBookmark(bookmark: Bookmark)
}