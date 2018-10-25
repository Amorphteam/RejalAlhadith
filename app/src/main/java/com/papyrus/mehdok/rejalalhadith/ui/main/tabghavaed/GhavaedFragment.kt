package com.papyrus.mehdok.rejalalhadith.ui.main.tabghavaed

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
import kotlinx.android.synthetic.main.activity_text_viewer.*
import kotlinx.android.synthetic.main.fragment_ghavaed.*
import java.util.*

class GhavaedFragment : Fragment() {
    private var listener: OnTabItemClickListener? = null

    private val subscriptions: CompositeDisposable = CompositeDisposable()

    var adapter: GhavaedAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ghavaed, container, false)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnTabItemClickListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()

        subscriptions.clear()
        subscriptions.dispose()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ghavaedRecycler.setHasFixedSize(true)
        adapter = GhavaedAdapter(ArrayList(), listener)
        val layoutManager = LinearLayoutManager(context)
        ghavaedRecycler.layoutManager = layoutManager
        ghavaedRecycler.adapter = adapter

        loadGhavaed()
    }

    fun loadGhavaed() {
        subscriptions.add(
                DataRepositoryImpl
                        .getInstance()
                        .getGhavaeds()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ ghavaed ->
                            adapter?.addItems(ghavaed)

                        }, { e ->
                            e.printStackTrace()
                        })
        )
    }


    companion object {
        @JvmStatic
        fun newInstance() =
                GhavaedFragment().apply {
                    arguments = Bundle().apply {}
                }
    }
}
