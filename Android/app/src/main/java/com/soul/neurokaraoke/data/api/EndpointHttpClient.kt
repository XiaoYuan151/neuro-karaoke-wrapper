package com.soul.neurokaraoke.data.api

import okhttp3.OkHttpClient

object EndpointHttpClient {
    val client: OkHttpClient by lazy {
        OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request()
            chain.proceed(request.newBuilder().url(EndpointRouter.route(request.url.toString())).build())
        }.build()
    }
}
