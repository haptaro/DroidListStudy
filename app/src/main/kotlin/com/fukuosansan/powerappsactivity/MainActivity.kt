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

    private lateinit var myListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myListView = findViewById<ListView>(R.id.myListView)

        val retrofit = Retrofit.Builder()
                .client(OkHttpClient())
                .baseUrl("https://connpass.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val apiClient: ConnpassApi = retrofit.create(ConnpassApi::class.java)

        val itemList = listOf("あいうえお", "かきくけこ", "さしすせそ", "たちつてと", "なにぬねの", "はひふへほ", "まみむめも", "やいゆえよ")

        handleResponse(itemList)

        apiClient
                .fetchEvent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ event ->

                    print(event)
                })
    }

    private fun handleResponse(itemList: List<String>) {
        val myListViewAdapter = MyListViewAdapter(this)

        for (item in itemList) {
            myListViewAdapter.add(item)
        }

        myListView.adapter = myListViewAdapter
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        println("=====: アイテムがクリックされました!!!")
        val intent = Intent(this, DetailActivity::class.java)
        startActivity(intent)
    }
}
