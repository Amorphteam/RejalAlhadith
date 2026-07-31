package org.masaha.rejalalhadith.database

import io.reactivex.Completable
import io.reactivex.Observable

interface DataRepository {
    fun getRejals(): Observable<List<RejalLink>>
    fun getRejals(page: Int): Observable<List<RejalLink>>
    fun getRejals(page: Int, keyword: String): Observable<List<RejalLink>>
    fun getRejals(keyword: String): Observable<List<RejalLink>>
    fun getRejal(id: Int): Observable<RejalLink?>
    fun getRejalsByIds(ids: List<Int>): Observable<List<RejalLink>>
    fun getGhavaeds(): Observable<List<RejalGhavaed>>
    fun getGhavaeds(page: Int): Observable<List<RejalGhavaed>>

    fun addBookmark(bookmark: Bookmark): Completable
    fun deleteBookmark(id: Int): Completable
    fun getBookmarkList(): Observable<List<Bookmark>>
}