package com.ahmed.vodafonerepos.retrofit

import com.ahmed.vodafonerepos.utils.Constants.Headers.ACCEPT
import com.ahmed.vodafonerepos.utils.Constants.Headers.ACCEPT_VALUE
import com.ahmed.vodafonerepos.utils.Constants.Headers.AUTHORIZATION
import com.ahmed.vodafonerepos.utils.Constants.Headers.AUTHORIZATION_VALUE
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class HeaderInterceptor : Interceptor {

    /*init {
        System.loadLibrary("native-lib")
    }*/

//    external fun getAuthorizationValue(): String?

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request: Request = chain.request()
            .newBuilder()
            .addHeader(AUTHORIZATION,"token ${/*getAuthorizationValue() ?:*/ AUTHORIZATION_VALUE}")
            .addHeader(ACCEPT, ACCEPT_VALUE)
            .build()
        return chain.proceed(request)
    }
}