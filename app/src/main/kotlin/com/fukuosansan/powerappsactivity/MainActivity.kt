package com.fukuosansan.powerappsactivity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myListView = findViewById<ListView>(R.id.myListView)

        val myListViewAdapter = MyListViewAdapter(this)

        myListViewAdapter.add("あいうえお")
        myListViewAdapter.add("かきくけこ")
        myListViewAdapter.add("さしすせそ")
        myListViewAdapter.add("たちつてと")
        myListViewAdapter.add("なにぬねの")
        myListViewAdapter.add("はひふへほ")
        myListViewAdapter.add("まみむめも")
        myListViewAdapter.add("やいゆえよ")

        myListView.adapter = myListViewAdapter

        val retrofit = Retrofit.Builder()
                .client(OkHttpClient())
                .baseUrl("https://connpass.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val apiClient: ConnpassApi = retrofit.create(ConnpassApi::class.java)

        apiClient
                .fetchEvent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ event ->

                    print(event)
                })
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        println("=====: アイテムがクリックされました!!!")
        val intent = Intent(this, DetailActivity::class.java)
        startActivity(intent)
    }
}
