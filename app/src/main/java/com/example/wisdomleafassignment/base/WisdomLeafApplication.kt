package com.example.wisdomleafassignment.base

import android.app.Application
import com.example.wisdomleafassignment.helper.AppSignatureHelper
import com.example.wisdomleafassignment.helper.SharedPrefHelper
import com.example.wisdomleafassignment.network.ServiceGenerator
import com.example.wisdomleafassignment.network.WebService


//This is application class used to create some instances used in whole application
class WisdomLeafApplication : Application() {

    companion object {
        private lateinit var currentApplication: WisdomLeafApplication

        fun getInstance(): WisdomLeafApplication {
            return currentApplication
        }
    }

    override fun onCreate() {
        super.onCreate()
        currentApplication = this
        initAppSignHelper()
    }

    //@use Access token in header and get from local preferences
    fun getWebServiceInstance(): WebService {
        return getRetrofitInstance().createService(
            WebService::class.java,
            getSharedPrefInstance().read(SharedPrefHelper.KEY_ACCESS_TOKEN, "")
        );
    }

    // get instance of retrofit
    fun getRetrofitInstance(): ServiceGenerator {
        return ServiceGenerator.getInstance(this);
    }

    // get instance of preferences
    fun getSharedPrefInstance(): SharedPrefHelper {
        return SharedPrefHelper.getInstance(this);
    }

    // get key hash for otp auto fill
    private fun initAppSignHelper() {
        AppSignatureHelper(applicationContext).appSignatures;
    }

}

