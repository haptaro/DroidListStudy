package com.fukuosansan.powerappsactivity

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by fukuo on 2017/11/07.
 */
interface ConnpassApi {
    @GET("/api/v1/event")
    fun fetchEvent(@Query("keyword") keyword: String): Observable<ConnpassEvent>
}