package com.fukuosansan.powerappsactivity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class DetailActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val (_, title, _, place, description) = intent.getSerializableExtra("event") as Event

        (findViewById<TextView>(R.id.title)).text = title
        (findViewById<TextView>(R.id.description)).text = description
        (findViewById<TextView>(R.id.place)).text = place
    }


}
