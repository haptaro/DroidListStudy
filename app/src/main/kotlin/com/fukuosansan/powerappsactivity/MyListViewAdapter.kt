package com.fukuosansan.powerappsactivity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

/**
 * Created by fukuo on 2017/11/07.
 */
class MyListViewAdapter(context: Context): ArrayAdapter<String>(context, 0) {
    // あとでカスタムViewを取ってくるので、レイアウトインフレイターを持たせておく
    private val layoutInflater = LayoutInflater.from(context)


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var contentView = convertView

        if(contentView == null) {
            contentView = layoutInflater.inflate(R.layout.my_listview_item, parent, false)
        }

        contentView?.findViewById<TextView>(R.id.title)?.text = getItem(position)

        return contentView!!
    }
}