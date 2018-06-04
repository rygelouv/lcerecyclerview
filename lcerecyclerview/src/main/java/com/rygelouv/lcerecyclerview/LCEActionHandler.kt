package com.rygelouv.lcerecyclerview

import android.util.SparseArray
import android.view.View

/**
 * Created by rygelouv on 6/1/18.
 * <p>
 * lce-recyclerview
 * Copyright (c) 2018 Makeba Inc All rights reserved.
 */
class LCEActionHandler {
    private val clickActionArray : SparseArray<ActionClickListener> by lazy {
        SparseArray<ActionClickListener>()
    }

    fun register(id: Int, listener: ActionClickListener) {
        clickActionArray.append(id, listener)
    }

    fun enableAction(rootView: View) {
        for (i in 0 until clickActionArray.size()) {
            val key = clickActionArray.keyAt(i)
            val view = rootView.findViewById<View>(key)
            view?.setOnClickListener { clickActionArray.get(key).onClick(view) }
        }
    }

    interface ActionClickListener {
        fun onClick(view: View)
    }
}
