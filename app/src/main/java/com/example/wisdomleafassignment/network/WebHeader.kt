package com.example.wisdomleafassignment.network

class WebHeader private constructor() {

    companion object {
        // headers
        const val KEY_CONTENT_TYPE = "Content-Type"
        const val KEY_ACCEPT = "Accept"
        const val KEY_AUTHORIZATION = "Authorization"
        const val KEY_PLATFORM = "platform"

        const val VAL_CONTENT_TYPE = "application/x-www-form-urlencoded"
        const val VAL_ACCEPT = "application/json"
        const val VAL_PLATFORM = "android"

    }

}
