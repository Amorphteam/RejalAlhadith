package org.masaha.rejalalhadith.database

import com.raizlabs.android.dbflow.sql.language.SQLite
import io.reactivex.Completable
import io.reactivex.Observable

class DataRepositoryImpl private constructor() : DataRepository {
    private val pageLimitCount = 100

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
                    .offset(start)
                    .limit(pageLimitCount)
                    .queryList()

            subscriber.onNext(list)
            subscriber.onComplete()
        }
    }

    override fun getRejals(keyword: String): Observable<List<RejalLink>> {
        return Observable.create { subscriber ->

            val list = SQLite.select()
                    .from(RejalLink::class.java)
                    .where(RejalLink_Table.name.like("%$keyword%"))
                    .queryList()

            subscriber.onNext(list)
            subscriber.onComplete()
        }
    }

    override fun getRejal(id: Int): Observable<RejalLink?> {
        return Observable.create { subscriber ->
            val rejal = SQLite.select()
                    .from(RejalLink::class.java)
                    .where(RejalLink_Table.ID.`is`(id))
                    .querySingle()

            if (rejal != null) {
                subscriber.onNext(rejal)
            }
            subscriber.onComplete()
        }
    }

    override fun getRejalsByIds(ids: List<Int>): Observable<List<RejalLink>> {
        return Observable.create { subscriber ->
            if (ids.isEmpty()) {
                subscriber.onNext(emptyList())
                subscriber.onComplete()
                return@create
            }

            val list = SQLite.select()
                    .from(RejalLink::class.java)
                    .where(RejalLink_Table.ID.`in`(ids))
                    .queryList()

            subscriber.onNext(list)
            subscriber.onComplete()
        }
    }

    override fun addBookmark(bookmark: Bookmark): Completable {
        return Completable.create { subscriber ->
            run {
                bookmark.save()
                subscriber.onComplete()
            }
        }

    }

    override fun deleteBookmark(id: Int): Completable {
        return Completable.create { subscriber ->
            run {
                SQLite.delete().from(Bookmark::class.java)
                        .where(Bookmark_Table.bookmarkId.`is`(id))
                        .execute()
                subscriber.onComplete()
            }
        }
    }

    override fun getBookmarkList(): Observable<List<Bookmark>> {
        return Observable.create { subscriber ->
            val list = SQLite.select()
                    .from(Bookmark::class.java)
                    .queryList()

            subscriber.onNext(list)
            subscriber.onComplete()
        }
    }


}