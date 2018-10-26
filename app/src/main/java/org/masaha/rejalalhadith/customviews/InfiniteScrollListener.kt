package org.masaha.rejalalhadith.customviews

interface InfiniteScrollListener {
    /**
     * When the RecyclerView getting scrolled to the triggerAmount this method is getting called, so the end of the
     * list is near and you need to add more content
     */
    fun loadMoreContent(page: Int)
}