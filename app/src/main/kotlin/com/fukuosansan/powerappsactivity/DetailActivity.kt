package com.fukuosansan.powerappsactivity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val title = intent.getStringExtra("タイトル")
        val description = intent.getStringExtra("説明")
        val place = intent.getStringExtra("場所")

        (findViewById<TextView>(R.id.title)).text = title
        (findViewById<TextView>(R.id.description)).text = description
        (findViewById<TextView>(R.id.place)).text = place

    }
}
