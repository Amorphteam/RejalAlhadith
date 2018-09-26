package com.papyrus.mehdok.rejalalhadith.ui.main.tabbookmark

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.papyrus.mehdok.rejalalhadith.R
import com.papyrus.mehdok.rejalalhadith.ui.main.OnTabItemClickListener

class BookmarkFragment : Fragment() {
    private var listener: OnTabItemClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmark, container, false)
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
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                BookmarkFragment().apply {
                    arguments = Bundle().apply {}
                }
    }
}
