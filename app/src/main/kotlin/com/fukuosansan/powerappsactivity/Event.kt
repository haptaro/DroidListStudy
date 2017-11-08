package com.fukuosansan.powerappsactivity

import com.google.gson.annotations.SerializedName

/**
 * Created by fukuo on 2017/11/07.
 */
data class Event (
        @SerializedName("event_id")
        val event_id: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("started_at")
        val startedAt: String,
        @SerializedName("place")
        val place: String,
        @SerializedName("description")
        val description: String
)

data class ConnpassEvent (
        @SerializedName("results_returned")
        val results: Int,
        @SerializedName("events")
        val events: List<Event>
)