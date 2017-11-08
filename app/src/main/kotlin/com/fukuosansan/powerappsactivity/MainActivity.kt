package com.fukuosansan.powerappsactivity

import android.app.ProgressDialog
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

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var myListView: ListView
    private lateinit var eventItems: List<Event>
    private lateinit var progressDialog: ProgressDialog
    private var userInputSearchText: String = ""
    private lateinit var searchText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myListView = findViewById<ListView>(R.id.myListView)

        myListView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("イベントID", eventItems[position].event_id)
            intent.putExtra("タイトル", eventItems[position].title)
            intent.putExtra("説明", eventItems[position].description)
            intent.putExtra("開始時間", eventItems[position].startedAt)
            intent.putExtra("場除", eventItems[position].place)
            startActivity(intent)
        }

        searchText = findViewById<EditText>(R.id.searchText)
        searchText.editableText.clear()
        searchText.hint = "検索対象"
        val searchButton = findViewById<Button>(R.id.searchButton)

        RxView.clicks(searchButton)
                .subscribe {
                    searchText.isFocusable = false
                    searchText.isFocusableInTouchMode = false
                    searchText.isEnabled = false
                    // ローディング開始
                    progressDialog = ProgressDialog.show(this, "通信中", "少々お待ち下さい", true)
                    sendRequest()
        }

        RxTextView.textChanges(searchText)
                .subscribe { text ->
                    userInputSearchText = text.toString()
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

        Log.d("入力", "=========== ユーザが入力した検索文字列: ${userInputSearchText}")
        apiClient
                .fetchEvent(keyword = userInputSearchText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { connpassEvent ->
                            // ローディング成功
                            progressDialog.dismiss()
                            eventItems = connpassEvent.events
                            handleResponse(eventItems)
                        },
                        { e ->
                            eventItems = createSeed()
                            Toast.makeText(this, "通信に失敗しました", Toast.LENGTH_SHORT).show()
                            handleResponse(eventItems)
                            // ローディング失敗
                            progressDialog.dismiss()
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
                Event(1, "あいうえお", "2000", "東京", "ほげ"),
                Event(2, "かきくけこ", "2001", "神奈川", "ふー"),
                Event(3, "たちつてと", "2002", "大阪", "ばー"),
                Event(4, "なにぬねの", "2003", "北海道", "ぴよ"),
                Event(5, "はひふへほ", "2004", "広島", "ボブ"),
                Event(6, "やいゆえよ", "2005", "沖縄", "太郎")
        )
        return eventItems
    }

}
