package com.fukuosansan.powerappsactivity

import com.google.gson.annotations.SerializedName

/**
 * Created by fukuo on 2017/11/07.
 */
data class Event (
        val event_id: String,
        val title: String,
        val startedAt: String
)

data class ConnpassEvent (
        @SerializedName("results_returned")
        val results: Int,
        @SerializedName("events")
        val events: List<Event>
)