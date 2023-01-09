package  com.example.wisdomleafassignment.network

import com.example.wisdomleafassignment.listItems.model.ListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface WebService {

    @GET(WebUrl.LIST_API)
    fun callGetImagesApi(@Query("page") page:Int,@Query("limit") limit:Int): Call<ListResponse>


}