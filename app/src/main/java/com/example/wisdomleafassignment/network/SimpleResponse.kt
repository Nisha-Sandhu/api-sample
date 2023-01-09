package  com.example.wisdomleafassignment.network

data class SimpleResponse(
    val data: Any,
    val message: String,
    val status: Int,
    val success: Boolean?

)