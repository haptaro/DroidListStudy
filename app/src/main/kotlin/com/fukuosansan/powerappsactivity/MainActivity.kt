package com.fukuosansan.powerappsactivity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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
            intent.putExtra("タイトル", eventItems[position].title)
            intent.putExtra("説明", eventItems[position].description)
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

        apiClient
                .fetchEvent(keyword = userInputSearchText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { connpassEvent ->
                            progressDialog.dismiss()
                            eventItems = connpassEvent.events
                            handleResponse(eventItems)
                        },
                        { e ->
                            Toast.makeText(this, "通信に失敗しました", Toast.LENGTH_SHORT).show()
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
}
