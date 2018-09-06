package com.rygelouv.lce_recyclerviewapp

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.rygelouv.lcerecyclerview.LCEActionHandler
import com.rygelouv.lcerecyclerview.LCERecyclerview
import com.rygelouv.lcerecyclerview.LayoutManagerType
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI

class MainActivity : AppCompatActivity() {

    lateinit var lceRecyclerview: LCERecyclerview
    val list = arrayListOf(
            User("Gerome", "Ndeko"),
            User("Gerome", "Ndeko"),
            User("Gerome", "Ndeko"),
            User("Gerome", "Ndeko"),
            User("Gerome", "Ndeko"),
            User("Gerome", "Ndeko"),
            User("Gerome", "Ndeko"),
            User("Gerome", "Ndeko"),
            User("Gerome", "Ndeko"),
            User("Gerome", "Ndeko"),
            User("Gerome", "Ndeko"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lceRecyclerview = findViewById(R.id.list)
        with(lceRecyclerview) {
            provideEmptyView(R.layout.empty_layout)
            provideErrorView(R.layout.error_layout)
            provideLoadingView(R.layout.loading_layout)
            provideRetryAction(R.id.retry, object : LCEActionHandler.ActionClickListener {
                override fun onClick(view: View) {
                    lceRecyclerview.isLoading()
                    launch(CommonPool) {
                        val finished = async {
                            delay(1000L)
                            true
                        }
                        if (finished.await()) {
                            withContext(UI) {
                                isSearchEmpty("No query found")
                                /*val adapter = UserListAdapter(list, this@MainActivity)
                                lceRecyclerview.setAdapter(adapter)*/
                            }
                        }
                    }
                }
            })
            provideLayoutManager(LayoutManagerType.GRID, 4)
            provideSearchEmptyView(R.layout.search_empty_view, dynamicText = true)
        }
        launch(CommonPool) {
            val finished = async {
                delay(3000L)
                true
            }
            if (finished.await()) {
                withContext(UI) {
                    val adapter = UserListAdapter(list, this@MainActivity)
                    //lceRecyclerview.setAdapter(adapter)
                    lceRecyclerview.isError()
                }
            }
        }
    }

    class UserListAdapter(var mDataSet: ArrayList<User>, var mContext: Context?): RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_user, parent, false))

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.firstname?.text = mDataSet[position].firstname
            holder.lastname?.text = mDataSet[position].lastname
        }

        override fun getItemCount(): Int = if (mDataSet.isEmpty()) 0 else mDataSet.size

        class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            var firstname = itemView?.findViewById<TextView>(R.id.firstname)
            var lastname = itemView?.findViewById<TextView>(R.id.lastname)
        }
    }

    data class User(var lastname: String, var firstname: String)
}
