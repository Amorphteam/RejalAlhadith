package com.papyrus.mehdok.rejalalhadith.database

import com.raizlabs.android.dbflow.sql.language.SQLite
import io.reactivex.Observable

class DataRepositoryImpl private constructor() : DataRepository {
    val pageLimitCount = 100

    companion object {
        private val mInstance: DataRepositoryImpl = DataRepositoryImpl()

        @Synchronized
        fun getInstance(): DataRepositoryImpl {
            return mInstance
        }
    }

    override fun getGhavaeds(): Observable<List<RejalGhavaed>> {
        return Observable.create { subscriber ->
            val list = SQLite.select()
                    .from(RejalGhavaed::class.java)
                    .queryList()

            subscriber.onNext(list)
            subscriber.onComplete()
        }
    }

    override fun getGhavaeds(page: Int): Observable<List<RejalGhavaed>> {
        return Observable.create { subscriber ->
            val start = page * pageLimitCount

            val list = SQLite.select()
                    .from(RejalGhavaed::class.java)
                    .offset(start)
                    .limit(pageLimitCount)
                    .queryList()

            subscriber.onNext(list)
            subscriber.onComplete()
        }
    }

    override fun getRejals(): Observable<List<RejalLink>> {
        return Observable.create { subscriber ->
            val list = SQLite.select()
                    .from(RejalLink::class.java)
                    .queryList()

            subscriber.onNext(list)
            subscriber.onComplete()
        }
    }

    override fun getRejals(page: Int): Observable<List<RejalLink>> {
        return Observable.create { subscriber ->
            val start = page * pageLimitCount

            val list = SQLite.select()
                    .from(RejalLink::class.java)
                    .offset(start)
                    .limit(pageLimitCount)
                    .queryList()

            subscriber.onNext(list)
            subscriber.onComplete()
        }
    }

    override fun getRejals(page: Int, keyword: String): Observable<List<RejalLink>> {
        return Observable.create { subscriber ->
            val start = page * pageLimitCount

            val list = SQLite.select()
                    .from(RejalLink::class.java)
                    .where(RejalLink_Table.name.like("%$keyword%"))
                    .or(RejalLink_Table.name2.like("%$keyword%"))
                    .or(RejalLink_Table.det.like("%$keyword%"))
                    .offset(start)
                    .limit(pageLimitCount)
                    .queryList()

            subscriber.onNext(list)
            subscriber.onComplete()
        }
    }
}