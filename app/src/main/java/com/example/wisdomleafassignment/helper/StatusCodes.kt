package com.example.wisdomleafassignment.helper

data class StatusCodes(
    val Status: List<StatusModel>?
) {
    data class StatusModel(
        val StatusCode: Int?,
        val StatusMessage: String?,
        val StatusType: String?
    )
}