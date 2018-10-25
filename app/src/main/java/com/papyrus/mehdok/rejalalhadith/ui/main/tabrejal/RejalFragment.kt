package com.papyrus.mehdok.rejalalhadith.ui.main.tabrejal


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.papyrus.mehdok.rejalalhadith.R
import com.papyrus.mehdok.rejalalhadith.customviews.InfiniteScrollListener
import com.papyrus.mehdok.rejalalhadith.database.DataRepositoryImpl
import com.papyrus.mehdok.rejalalhadith.ui.main.OnTabItemClickListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_rejal.*
import java.util.*

class RejalFragment : Fragment(), InfiniteScrollListener {
    private var listener: OnTabItemClickListener? = null

    private val subscriptions: CompositeDisposable = CompositeDisposable()

    private var adapter: RejalAdapter? = null

    private var pageCount = 0
    private var loading = false

    private var isSearching = false
    private var searchQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rejal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rejalRecycler.infiniteScrollListener = this
        rejalRecycler.setHasFixedSize(false)
        adapter = RejalAdapter(ArrayList(), listener)
        val layoutManager = LinearLayoutManager(context)
        rejalRecycler.layoutManager = layoutManager
        rejalRecycler.adapter = adapter

        loadRejals(0)
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

    override fun loadMoreContent(page: Int) {
        if (!loading) {
            if (isSearching) {
                searchRejals(searchQuery, ++pageCount)
            } else {
                loadRejals(++pageCount)
            }
        }
    }

    fun loadRejals(page: Int) {
        loading = true

        subscriptions.add(
                DataRepositoryImpl
                        .getInstance()
                        .getRejals(page)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ rejals ->
                            adapter?.addItems(rejals)

                            loading = false
                        }, { e ->
                            e.printStackTrace()
                        })
        )

    }

    fun searchRejals(query: String, page: Int = 0) {
        searchQuery = query
        adapter?.searchQuery = query

        if (page == 0) {
            // if it is first search attempt clear all items
            adapter?.removeAllItem()
        }

        if (query.isEmpty()) {
            isSearching = false
            loading = false
            pageCount = 0
            loadRejals(pageCount)
            return
        }

        pageCount = page
        isSearching = true
        loading = true

        subscriptions.add(
                DataRepositoryImpl
                        .getInstance()
                        .getRejals(pageCount, query)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ rejals ->
                            adapter?.addItems(rejals)

                            loading = false
                        }, { e ->
                            e.printStackTrace()
                        })
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                RejalFragment().apply {
                    arguments = Bundle().apply {}
                }
    }
}
