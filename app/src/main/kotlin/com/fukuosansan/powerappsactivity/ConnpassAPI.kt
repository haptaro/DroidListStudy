package com.fukuosansan.powerappsactivity

import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by fukuo on 2017/11/07.
 */
interface ConnpassApi {
    @GET("/api/v1/event")
    fun fetchEvent(): Observable<ConnpassEvent>
}