package com.example.wisdomleafassignment.helper

import android.app.Activity
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.example.wisdomleafassignment.base.SingletonHolder
import com.example.wisdomleafassignment.base.WisdomLeafApplication

/**
 * This is shared prefences class used to store some values in local storage like access token and used detail
 * it have read and write methods foe int, string and boolean values. You can also create methods to store other types of data.
 */

class SharedPrefHelper private constructor(var application: WisdomLeafApplication) {


    private var sharedPref: SharedPreferences =
        application.getSharedPreferences(application.getPackageName(), Activity.MODE_PRIVATE)


    companion object :
        SingletonHolder<SharedPrefHelper, WisdomLeafApplication>(::SharedPrefHelper) {

        const val KEY_LOGGED_IN = "key_logged_in"
        const val KEY_ACCESS_TOKEN = "key_access_token"
        const val KEY_IS_ONLINE = "key_is_online"

    }

    fun read(key: String?, defValue: String?): String? {
        return sharedPref.getString(key, defValue)
    }

    fun write(key: String, value: String) {
        val prefsEditor: Editor = sharedPref.edit()
        prefsEditor.putString(key, value)
        prefsEditor.apply()
    }

    fun read(key: String, defValue: Boolean): Boolean {
        return sharedPref.getBoolean(key, defValue)
    }

    fun write(key: String, value: Boolean) {
        val prefsEditor: Editor = sharedPref.edit()
        prefsEditor.putBoolean(key, value)
        prefsEditor.apply()
    }
//
//    fun write(key: String, value: LoginResponse.Data) {
//        val prefsEditor: Editor = sharedPref.edit()
//        val gson = Gson()
//        val json = gson.toJson(value)
//        prefsEditor.putString(key, json)
//        prefsEditor.apply()
//    }
//
//    fun read(key: String?): LoginResponse.Data? {
//        val gson = Gson()
//        val json = gson.fromJson(sharedPref.getString(key, null), LoginResponse.Data::class.java)
//        return json
//    }


    fun read(key: String?, defValue: Long): Long? {
        return sharedPref.getLong(key, defValue)
    }


    fun read(key: String?, defValue: Int): Int? {
        return sharedPref.getInt(key, defValue)
    }

    fun write(key: String?, value: Long) {
        val prefsEditor: Editor = sharedPref.edit()
        prefsEditor.putLong(key, value)
        prefsEditor.apply()
    }

    fun write(key: String?, value: Int) {
        val prefsEditor: Editor = sharedPref.edit()
        prefsEditor.putInt(key, value)
        prefsEditor.apply()
    }

    fun clear() {
        val prefsEditor: Editor = sharedPref.edit()
        prefsEditor.clear()
        prefsEditor.apply()
    }


}