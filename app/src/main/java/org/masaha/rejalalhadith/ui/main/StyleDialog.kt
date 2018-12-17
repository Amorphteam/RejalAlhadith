package org.masaha.rejalalhadith.ui.main

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDialogFragment
import android.view.*
import android.widget.ImageView
import com.papyrus.mehdok.rejalalhadith.R

class StyleDialog : AppCompatDialogFragment() {
    interface ClickListener {
        fun increaseFontSize()
        fun decreaseFontSize()
    }

    private var topBorder: Int = 0
    private var clickListener: ClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            topBorder = arguments!!.getInt(TOP_BORDER)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val content = inflateLayout()

        content.findViewById<ImageView>(R.id.increase_font_size)?.setOnClickListener {
            clickListener?.increaseFontSize()
        }

        content.findViewById<ImageView>(R.id.decrease_font_size)?.setOnClickListener {
            clickListener?.decreaseFontSize()
        }

        val styleDialog = AlertDialog.Builder(context!!, R.style.StyleMenuStyle)
                .setView(content).create()

        val wmlp = styleDialog.window!!.attributes
        if(getText(R.string.ltr)=="true"){
            wmlp.gravity = Gravity.TOP or Gravity.RIGHT
        }else{
            wmlp.gravity = Gravity.TOP or Gravity.LEFT
        }
        wmlp.x = 0   //x position
        wmlp.y = topBorder   //y position
        wmlp.flags = wmlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()

        return styleDialog
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        Log.d("onViewCreated", "onViewCreated")
//        increase_font_size.setOnClickListener {
//            clickListener?.increaseFontSize()
//        }
//
//        decrease_font_size.setOnClickListener {
//            clickListener?.decreaseFontSize()
//        }
//    }

    private fun inflateLayout(): View {
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return inflater.inflate(R.layout.style_menu, view as ViewGroup?, false)
    }

    fun setClickListener(clickListener: ClickListener): StyleDialog {
        this.clickListener = clickListener
        return this
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clickListener = null
    }

    companion object {
        private val TOP_BORDER = "top_border"

        fun newInstance(top: Int): StyleDialog {
            val dialog = StyleDialog()
            val bundle = Bundle()
            bundle.putInt(TOP_BORDER, top)
            dialog.arguments = bundle

            return dialog
        }
    }
}