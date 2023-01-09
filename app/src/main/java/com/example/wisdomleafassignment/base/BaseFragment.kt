package com.example.wisdomleafassignment.base

import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.wisdomleafassignment.R
import com.google.android.material.snackbar.Snackbar

/*
* This is BaseFragment used in all fragments in the application
* and have some basic methods required in all activities
* {@link showSnackBar(msg:String, @ColorRes colorId:Int= R.color.colorAccent)}
* {@link View.isUserInteractionEnabled(enabled: Boolean)}
* */
abstract class BaseFragment : Fragment()  {

    fun showSnackBar(msg:String, @ColorRes colorId:Int= R.color.white){
        val snackBar= Snackbar.make(getRootView(), msg, Snackbar.LENGTH_LONG)
        snackBar.view.setBackgroundColor(ContextCompat.getColor(requireActivity(),colorId))
        snackBar.show()
    }

     abstract fun getRootView(): View

    // Extension function to enable and disable UI while loading
    fun View.isUserInteractionEnabled(enabled: Boolean) {
        isEnabled = enabled
        if (this is ViewGroup && this.childCount > 0) {
            this.children.forEach {
                it.isUserInteractionEnabled(enabled)
            }
        }
    }






}