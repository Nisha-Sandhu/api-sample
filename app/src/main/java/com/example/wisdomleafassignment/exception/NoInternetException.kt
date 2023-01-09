package com.example.wisdomleafassignment.exception

import java.io.IOException

/**
 * This is No internet exception class which directly implemented in service generator
 */

class NoInternetException : IOException() {

    override val message: String
        get() = "No Internet Connection"


}