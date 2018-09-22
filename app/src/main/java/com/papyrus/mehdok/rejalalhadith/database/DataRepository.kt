package com.papyrus.mehdok.rejalalhadith.database

import io.reactivex.Observable

interface DataRepository {
    fun getRejals(): Observable<List<RejalLink>>
    fun getRejals(page: Int): Observable<List<RejalLink>>
    fun getRejals(page: Int, keyword: String): Observable<List<RejalLink>>
    fun getGhavaeds(): Observable<List<RejalGhavaed>>
    fun getGhavaeds(page: Int): Observable<List<RejalGhavaed>>

}