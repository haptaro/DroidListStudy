package com.fukuosansan.powerappsactivity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ListView

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
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        println("=====: アイテムがクリックされました!!!")
        val intent = Intent(this, DetailActivity::class.java)
        startActivity(intent)
    }
}
