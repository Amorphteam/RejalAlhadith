package org.masaha.rejalalhadith.database

import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.sql.language.SQLite
import io.reactivex.Completable
import io.reactivex.Observable
import org.masaha.rejalalhadith.utils.ArabicTextNormalizer
import org.masaha.rejalalhadith.utils.SearchMode

class DataRepositoryImpl private constructor() : DataRepository {
    private val pageLimitCount = 100

    @Volatile
    private var searchCacheKey: String? = null
    @Volatile
    private var searchCacheIds: List<Int> = emptyList()

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

    override fun getRejals(page: Int, keyword: String, mode: SearchMode): Observable<List<RejalLink>> {
        return Observable.create { subscriber ->
            val ids = matchingRejalIds(keyword, mode)
            val start = page * pageLimitCount
            if (start >= ids.size) {
                subscriber.onNext(emptyList())
            } else {
                val pageIds = ids.subList(start, minOf(start + pageLimitCount, ids.size))
                subscriber.onNext(loadRejalsPreservingOrder(pageIds))
            }
            subscriber.onComplete()
        }
    }

    override fun getRejals(keyword: String, mode: SearchMode): Observable<List<RejalLink>> {
        return Observable.create { subscriber ->
            subscriber.onNext(loadRejalsPreservingOrder(matchingRejalIds(keyword, mode)))
            subscriber.onComplete()
        }
    }

    /**
     * Arabic-aware search in Kotlin (same rules as [ArabicTextNormalizer]), so variants
     * like ا/أ/إ/آ and ه/ة always match regardless of SQLite/DBFlow SQL quirks.
     */
    private fun matchingRejalIds(keyword: String, mode: SearchMode): List<Int> {
        val trimmed = keyword.trim()
        val normalizedQuery = ArabicTextNormalizer.normalize(trimmed)
        if (normalizedQuery.isEmpty()) {
            return emptyList()
        }

        val cacheKey = mode.name + '\u0000' + trimmed
        synchronized(this) {
            if (searchCacheKey == cacheKey) {
                return searchCacheIds
            }
        }

        val column = when (mode) {
            SearchMode.NAME_STARTS_WITH, SearchMode.NAME_CONTAINS -> "name"
            SearchMode.DESCRIPTION -> "det"
        }

        val database = FlowManager.getDatabase(RejalDatabase::class.java).writableDatabase
        val cursor = database.rawQuery("SELECT `ID`, `$column` FROM `rejal`", null)
        val ids = ArrayList<Int>()
        try {
            val idIndex = cursor.getColumnIndex("ID")
            val textIndex = cursor.getColumnIndex(column)
            while (cursor.moveToNext()) {
                val text = cursor.getString(textIndex) ?: continue
                val normalizedText = ArabicTextNormalizer.normalize(text)
                val matches = when (mode) {
                    SearchMode.NAME_STARTS_WITH -> normalizedText.startsWith(normalizedQuery)
                    SearchMode.NAME_CONTAINS, SearchMode.DESCRIPTION ->
                        normalizedText.contains(normalizedQuery)
                }
                if (matches) {
                    ids.add(cursor.getInt(idIndex))
                }
            }
        } finally {
            cursor.close()
        }

        synchronized(this) {
            searchCacheKey = cacheKey
            searchCacheIds = ids
        }
        return ids
    }

    private fun loadRejalsPreservingOrder(ids: List<Int>): List<RejalLink> {
        if (ids.isEmpty()) {
            return emptyList()
        }
        val models = SQLite.select()
                .from(RejalLink::class.java)
                .where(RejalLink_Table.ID.`in`(ids))
                .queryList()
        val byId = models.associateBy { it.ID }
        return ids.mapNotNull { byId[it] }
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
