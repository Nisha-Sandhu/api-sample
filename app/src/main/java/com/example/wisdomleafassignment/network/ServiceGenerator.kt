package com.example.wisdomleafassignment.network

import android.util.Log
import com.example.wisdomleafassignment.BuildConfig
import com.example.wisdomleafassignment.base.SingletonHolder
import com.example.wisdomleafassignment.base.WisdomLeafApplication
import com.example.wisdomleafassignment.exception.NoInternetException
import com.example.wisdomleafassignment.extra.NetworkUtil
import com.google.gson.GsonBuilder

import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/*
 API main gateway
 All API calls went through this class
 This is customize service class
 you can add headers here, can pass values as well
*/
class ServiceGenerator private constructor(var application: WisdomLeafApplication) {
    private val TAG = "RetrofitManager"

    // endregion
    private val gson = GsonBuilder().setLenient().serializeNulls().create()

    private val retrofitBuilder =
        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson))

    private val defaultOkHttpClient = OkHttpClient.Builder().readTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).build()

    companion object :
        SingletonHolder<ServiceGenerator, WisdomLeafApplication>(::ServiceGenerator)

    // No need to instantiate this class.
    fun <S> createService(serviceClass: Class<S>?, baseUrl: String?, no: String?): S {
        return createService(serviceClass, baseUrl, MyOkHttpInterceptor(no))
    }

    fun <S> createService(serviceClass: Class<S>?, token: String?): S {
        return createService(serviceClass, WebUrl.BASE_URL, MyOkHttpInterceptor(token))
    }

    fun <S> createService(
        serviceClass: Class<S>?,
        baseUrl: String?,
        networkInterceptor: Interceptor?
    ): S {

        val okHttpClientBuilder = defaultOkHttpClient.newBuilder()
        if (networkInterceptor != null) {
            okHttpClientBuilder.addNetworkInterceptor(networkInterceptor)
        }
        okHttpClientBuilder.writeTimeout(120, TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(90, TimeUnit.SECONDS)
        val modifiedOkHttpClient = okHttpClientBuilder.addInterceptor(getHttpLoggingInterceptor()!!)
            .addInterceptor(getNetworkInterceptor()).build()
        // Install the all-trusting trust manager
        retrofitBuilder.client(modifiedOkHttpClient)
        retrofitBuilder.baseUrl(baseUrl!!)
        val retrofit = retrofitBuilder.build()
        return retrofit.create(serviceClass!!)
    }

    // check internet available or not
    private fun getNetworkInterceptor(): Interceptor {
        return object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val isConnected: Boolean = NetworkUtil.isNetAvail(application)
                if (!isConnected) {
                    throw NoInternetException(); // Throwing our custom exception 'InternetException'
                }
                val builder = chain.request().newBuilder()
                return chain.proceed(builder.build())
            }
        }
    }

    private fun getCache(): Cache? {
        var mCache: Cache? = null
        try {
            mCache = Cache(
                File(WisdomLeafApplication.getInstance().getCacheDir(), "http_cache"),
                10 * 1024 * 1024
            ) // 10 MB
        } catch (e: Exception) {
            Log.e(TAG, "Could not create Cache!")
        }
        return mCache
    }

    private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor? {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        return httpLoggingInterceptor
    }

//    private fun getUnsafeOkHttpClient(): OkHttpClient {
//        return try {
//            // Create a trust manager that does not validate certificate chains
//            val trustAllCerts =
//                arrayOf<TrustManager>(
//                    object : X509TrustManager {
//                        @SuppressLint("TrustAllX509TrustManager")
//                        @Throws(CertificateException::class)
//                        override fun checkClientTrusted(
//                            chain: Array<X509Certificate>,
//                            authType: String
//                        ) {
//                        }
//
//                        @SuppressLint("TrustAllX509TrustManager")
//                        @Throws(CertificateException::class)
//                        override fun checkServerTrusted(
//                            chain: Array<X509Certificate>,
//                            authType: String
//                        ) {
//                        }
//
//                        override fun getAcceptedIssuers(): Array<X509Certificate> {
//                            return arrayOf()
//                        }
//                    }
//                )
//            val x509TrustManager: X509TrustManager =
//                object : X509TrustManager {
//                    @Throws(CertificateException::class)
//                    override fun checkClientTrusted(
//                        chain: Array<X509Certificate>,
//                        authType: String
//                    ) {
//                    }
//
//                    @Throws(CertificateException::class)
//                    override fun checkServerTrusted(
//                        chain: Array<X509Certificate>,
//                        authType: String
//                    ) {
//                    }
//
//                    override fun getAcceptedIssuers(): Array<X509Certificate> {
//                        return arrayOf()
//                    }
//                }
//
//            // Install the all-trusting trust manager
//            val sslContext = SSLContext.getInstance("SSL")
//            sslContext.init(null, trustAllCerts, SecureRandom())
//            // Create an ssl socket factory with our all-trusting manager
//            val sslSocketFactory = sslContext.socketFactory
//            val builder = OkHttpClient.Builder()
//            builder.sslSocketFactory(
//                sslSocketFactory,
//                x509TrustManager
//            ) //, (X509TrustManager) trustAllCerts[0]
//            builder.hostnameVerifier(HostnameVerifier { hostname, session -> true })
//            builder.cache(getCache()).build()
//        } catch (e: Exception) {
//            throw RuntimeException(e)
//        }
//    }

    // Custom OkHttpInterceptor class to add headers.
    class MyOkHttpInterceptor internal constructor(token: String?) : Interceptor {
        var token: String? = ""

        init {
            this.token = token
        }

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val newRequest: Request?
            newRequest = if (token != null) {
                if (token!!.isNotEmpty() && token!!.contains("Bearer")) {
                    originalRequest.newBuilder()
                        .header(WebHeader.KEY_AUTHORIZATION, token!!)
                        .header(WebHeader.KEY_PLATFORM, WebHeader.VAL_PLATFORM)
                        .build()
                } else {
                    if (token!!.isNotEmpty()) if (token!!.contains("Basic")) {
                        originalRequest.newBuilder()
                            .header(WebHeader.KEY_AUTHORIZATION, token!!)
                            .header(WebHeader.KEY_PLATFORM, WebHeader.VAL_PLATFORM)
                            .build()
                    } else originalRequest.newBuilder()
                        .header(WebHeader.KEY_AUTHORIZATION, "Bearer $token")
                        .header(WebHeader.KEY_PLATFORM, WebHeader.VAL_PLATFORM)
                        .build()
                    else originalRequest.newBuilder()

                        .header(WebHeader.KEY_PLATFORM, WebHeader.VAL_PLATFORM)
                        .build()
                }
            } else originalRequest.newBuilder()
                .header(WebHeader.KEY_PLATFORM, WebHeader.VAL_PLATFORM)
                .build()

            return chain.proceed(newRequest)
        }


    }
}


