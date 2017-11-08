package com.fukuosansan.powerappsactivity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var myListView: ListView
    private lateinit var eventItems: List<Event>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myListView = findViewById<ListView>(R.id.myListView)

        myListView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("文字列", eventItems[position].title)
            startActivity(intent)
        }

        val searchText = findViewById<EditText>(R.id.searchText)
        searchText.editableText.clear()
        searchText.isFocusable = false
        val searchButton = findViewById<Button>(R.id.searchButton)

        RxView.clicks(searchButton)
                .subscribe {
                    sendRequest()
        }

        RxTextView.textChanges(searchText)
                .subscribe { text ->
                    Log.d("入力", "===== 入力: ${text}")
                }
    }

    private fun sendRequest() {
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
                .subscribe(
                        { connpassEvent ->
                            eventItems = connpassEvent.events
                            handleResponse(eventItems)
                        },
                        { e ->
                            eventItems = createSeed()
                            Toast.makeText(this, "通信に失敗しました", Toast.LENGTH_SHORT)
                            handleResponse(eventItems)
                        })
    }

    private fun handleResponse(itemList: List<Event>) {
        val myListViewAdapter = MyListViewAdapter(this)

        for (item in itemList) {
            myListViewAdapter.add(item)
        }

        myListView.adapter = myListViewAdapter
    }

    // 仮のseedData作成関数
    private fun createSeed(): List<Event> {
        val eventItems = listOf<Event>(
                Event("1", "あいうえお", "2000", "東京", "ほげ"),
                Event("2", "かきくけこ", "2001", "東京", "ほげ"),
                Event("1", "たちつてと", "2002", "東京", "ほげ"),
                Event("1", "なにぬねの", "2003", "東京", "ほげ"),
                Event("1", "はひふへほ", "2004", "東京", "ほげ"),
                Event("1", "やいゆえよ", "2004", "東京", "ほげ")
        )
        return eventItems
    }

}
