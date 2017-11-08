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
class MyListViewAdapter(context: Context): ArrayAdapter<Event>(context, 0) {
    // あとでカスタムViewを取ってくるので、レイアウトインフレイターを持たせておく
    private val layoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var contentView = convertView

        if(contentView == null) {
            contentView = layoutInflater.inflate(R.layout.my_listview_item, parent, false)
        }

        contentView?.findViewById<TextView>(R.id.title)?.text = getItem(position).title
        contentView?.findViewById<TextView>(R.id.startedAt)?.text = getItem(position).startedAt
        contentView?.findViewById<TextView>(R.id.eventId)?.text = getItem(position).event_id as String
        contentView?.findViewById<TextView>(R.id.place)?.text = getItem(position).place
        contentView?.findViewById<TextView>(R.id.description)?.text = getItem(position).description

        return contentView!!
    }
}