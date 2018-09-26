package com.papyrus.mehdok.rejalalhadith.ui.main.tabbookmark

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.papyrus.mehdok.rejalalhadith.R
import com.papyrus.mehdok.rejalalhadith.database.DataRepositoryImpl
import com.papyrus.mehdok.rejalalhadith.ui.main.OnTabItemClickListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_bookmark.*
import java.util.*

class BookmarkFragment : Fragment() {
    private val subscriptions: CompositeDisposable = CompositeDisposable()

    private var listener: OnTabItemClickListener? = null

    private var adapter: BookmarkAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmark, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookmarkRecycler.setHasFixedSize(false)
        adapter = BookmarkAdapter(ArrayList())
        val layoutManager = LinearLayoutManager(context)
        bookmarkRecycler.layoutManager = layoutManager
        bookmarkRecycler.adapter = adapter

        loadBookmarks()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnTabItemClickListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null

        subscriptions.clear()
        subscriptions.dispose()
    }

    fun loadBookmarks() {
        subscriptions.add(
                DataRepositoryImpl
                        .getInstance()
                        .getBookmarkList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ bookmarks ->
                            adapter?.addItems(bookmarks)
                        }, { e ->
                            e.printStackTrace()
                        })
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                BookmarkFragment().apply {
                    arguments = Bundle().apply {}
                }
    }
}
