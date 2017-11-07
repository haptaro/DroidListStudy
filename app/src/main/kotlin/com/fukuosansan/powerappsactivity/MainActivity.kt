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

        val events = listOf<Event>(
                Event("あ", "あいうえお", "2000"),
                Event("か", "かきくけこ", "2001"),
                Event("さ", "さしすせそ", "2002"),
                Event("た", "たちつてと", "2003"),
                Event("な", "なにぬねの", "2004"),
                Event("は", "はひふへほ", "2005"),
                Event("ま", "まみむめも", "2006"),
                Event("や", "やいゆえよ", "2007")
                )

        handleResponse(events)

        apiClient
                .fetchEvent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ connpassEvent ->

                    print(connpassEvent.events)
                    handleResponse(connpassEvent.events)

                })
    }

    private fun handleResponse(itemList: List<Event>) {
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
