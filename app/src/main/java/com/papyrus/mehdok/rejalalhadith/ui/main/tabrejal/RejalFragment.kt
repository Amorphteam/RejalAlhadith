package com.papyrus.mehdok.rejalalhadith.ui.main.tabrejal


import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.papyrus.mehdok.rejalalhadith.R
import com.papyrus.mehdok.rejalalhadith.customviews.InfiniteScrollListener
import com.papyrus.mehdok.rejalalhadith.database.DataRepositoryImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_rejal.*
import java.util.*

class RejalFragment : Fragment(), InfiniteScrollListener {
    private val subscriptions: CompositeDisposable = CompositeDisposable()

    private var listener: OnFragmentInteractionListener? = null
    private var adapter: RejalAdapter? = null

    private var pageCount = 1
    private var loading = false

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
        adapter = RejalAdapter(ArrayList())
        val layoutManager = LinearLayoutManager(context)
        rejalRecycler.layoutManager = layoutManager
        rejalRecycler.adapter = adapter

        loadRejals(1)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
//            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null

        subscriptions.clear()
        subscriptions.dispose()
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    override fun loadMoreContent(page: Int) {
        if (!loading) {
            loadRejals(++pageCount)
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

    companion object {
        @JvmStatic
        fun newInstance() =
                RejalFragment().apply {
                    arguments = Bundle().apply {}
                }
    }
}
