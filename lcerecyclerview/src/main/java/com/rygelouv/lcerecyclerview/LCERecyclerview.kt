package com.rygelouv.lcerecyclerview

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v7.widget.GridLayoutManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView


/**
 * Created by rygelouv on 5/30/18.
 * <p>
 * lce-recyclerviewapp
 * Copyright (c) 2018 Makeba Inc All rights reserved.
 */
class LCERecyclerview: RelativeLayout {

    companion object {
        const val EMPTY_VIEW = "empty_view"
        const val LOADING_VIEW = "loading_view"
        const val ERROR_VIEW = "error_view"
        const val RECYCLER_LAYOUT = "com.rygelouv.lcerecyclerview"
        const val STATE_SUPER_CLASS = "SuperClassState"
    }

    private lateinit var mEmptyView: View
    private var mSearchEmptyView: View?  = null
    private lateinit var mErrorView: View
    private lateinit var mLoadingView: View
    lateinit var mRecyclerView: RecyclerView
    var mSearchEmptyText: TextView? = null
    var layoutManager: LayoutManagerType? = null
    var gridNumber: Int = 2
    var isSeachable : Boolean = false

    private val lCEActionHandler by lazy {
        LCEActionHandler()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        inflate(context, R.layout.lce_recycler_view_layout, this)
        mEmptyView = findViewById(R.id.empty_view)
        mSearchEmptyView = findViewById(R.id.search_empty_view)
        mErrorView = findViewById(R.id.error_view)
        mLoadingView = findViewById(R.id.loading_view)
        mRecyclerView = findViewById(R.id.content_view)
    }

    fun isEmpty() {
        mRecyclerView.visibility = View.GONE
        mErrorView.visibility = View.GONE
        mLoadingView.visibility = View.GONE
        mSearchEmptyView?.visibility = View.GONE
        mEmptyView.visibility = View.VISIBLE
    }

    fun isSearchEmpty(text: String? = null) {
        mRecyclerView.visibility = View.GONE
        mEmptyView.visibility = View.GONE
        mErrorView.visibility = View.GONE
        mLoadingView.visibility = View.GONE
        mSearchEmptyView?.visibility = View.VISIBLE

       mSearchEmptyText?.let {
           if (!text.isNullOrEmpty()) {
               it.text = text
           }
       }
    }

    private fun addViewIncenter(view: View) {
        val eLayoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        eLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
        view.layoutParams = eLayoutParams
        addView(view)
    }

    fun provideEmptyView(view: View) {
        mEmptyView = view
        addViewIncenter(mEmptyView)
        mEmptyView.visibility = View.GONE
    }

    fun provideEmptyView(@LayoutRes layout: Int, addViewInCenter: Boolean = true) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mEmptyView = inflater.inflate(layout, null, false)
        mEmptyView.visibility = View.GONE
        if (addViewInCenter) {
            addViewIncenter(mEmptyView)
        }
        else {
            addView(mEmptyView)
        }
    }

    fun provideSearchEmptyView(@LayoutRes layout: Int, addViewInCenter: Boolean = true, dynamicText: Boolean = false) {
        isSeachable = true
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mSearchEmptyView = inflater.inflate(layout, null, false)
        if (dynamicText) {
            mSearchEmptyText = mSearchEmptyView?.findViewById(R.id.search_text)
        }
        mSearchEmptyView?.visibility = View.GONE
        if (addViewInCenter) {
            addViewIncenter(mSearchEmptyView!!)
        }
        else {
            addView(mSearchEmptyView)
        }
    }

    fun provideErrorView(view: View) {
        mErrorView = view
        addViewIncenter(mErrorView)
        mErrorView.visibility = View.GONE
    }

    fun provideErrorView(@LayoutRes layout: Int) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mErrorView = inflater.inflate(layout, null, false)
        addViewIncenter(mErrorView)
        mErrorView.visibility = View.GONE
    }

    fun provideRetryAction(@IdRes retryButtonId: Int = 0, listener: LCEActionHandler.ActionClickListener) {
        lCEActionHandler.register(retryButtonId, listener)
        lCEActionHandler.enableAction(this)
    }

    fun provideLoadingView(view: View) {
        mLoadingView = view
        addViewIncenter(mLoadingView)
    }

    fun provideLoadingView(@LayoutRes layout: Int) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mLoadingView = inflater.inflate(layout, null, false)
        addViewIncenter(mLoadingView)
    }

    fun provideLayoutManager (managerType: LayoutManagerType, gridNumber: Int = 2) {
        layoutManager = managerType
        this.gridNumber = gridNumber
    }

    fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        mRecyclerView.layoutManager = when(layoutManager) {
            LayoutManagerType.LINEAR -> LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            LayoutManagerType.GRID -> GridLayoutManager(context, gridNumber)
            else -> LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        mRecyclerView.adapter = adapter
        checkIfEmpty()
    }

    private fun checkIfEmpty() {
        if (mRecyclerView.adapter != null) {
            val emptyViewVisible = mRecyclerView.adapter.itemCount == 0
            mRecyclerView.visibility = if (emptyViewVisible) View.GONE else View.VISIBLE
            mEmptyView.visibility = if (emptyViewVisible) View.VISIBLE else View.GONE

            mErrorView.visibility = View.GONE
            mLoadingView.visibility = View.GONE
        }
    }

    fun isLoading() {
        mLoadingView.visibility = View.VISIBLE
        mEmptyView.visibility = View.GONE
        mErrorView.visibility = View.GONE
        mRecyclerView.visibility = View.GONE
    }

    fun isError() {
        mLoadingView.visibility = View.GONE
        mEmptyView.visibility = View.GONE
        mErrorView.visibility = View.VISIBLE
        mRecyclerView.visibility = View.GONE
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable(STATE_SUPER_CLASS, super.onSaveInstanceState())
        bundle.putInt(EMPTY_VIEW, mEmptyView.visibility)
        bundle.putInt(ERROR_VIEW, mErrorView.visibility)
        bundle.putInt(LOADING_VIEW, mLoadingView.visibility)
        bundle.putParcelable(RECYCLER_LAYOUT, mRecyclerView.layoutManager?.onSaveInstanceState())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val bundle = state as Bundle
        super.onRestoreInstanceState(bundle.getParcelable(STATE_SUPER_CLASS))
        mEmptyView.visibility = if (bundle.getInt(EMPTY_VIEW) == View.VISIBLE) View.VISIBLE else View.GONE
        mErrorView.visibility = if (bundle.getInt(ERROR_VIEW) == View.VISIBLE) View.VISIBLE else View.GONE
        mLoadingView.visibility = if (bundle.getInt(LOADING_VIEW) == View.VISIBLE) View.VISIBLE else View.GONE
        val savedRecyclerLayoutState = bundle.getParcelable(RECYCLER_LAYOUT) as Parcelable?
        mRecyclerView.layoutManager?.onRestoreInstanceState(savedRecyclerLayoutState)
    }
}

enum class LayoutManagerType {
    LINEAR,
    GRID
}