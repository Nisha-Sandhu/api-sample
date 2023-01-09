package com.example.wisdomleafassignment.listItems.viewModel

import androidx.lifecycle.MutableLiveData
import com.example.wisdomleafassignment.base.BaseViewModel
import com.example.wisdomleafassignment.base.Status
import com.example.wisdomleafassignment.base.WisdomLeafApplication
import com.example.wisdomleafassignment.extra.WebResponse
import com.example.wisdomleafassignment.helper.ErrorHandler
import com.example.wisdomleafassignment.listItems.model.ListResponse
import com.example.wisdomleafassignment.network.SimpleResponse
import com.example.wisdomleafassignment.network.WebService

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListViewModel : BaseViewModel() {

    private var webService: WebService = WisdomLeafApplication.getInstance().getWebServiceInstance()

    private val listLiveDataPrivate = MutableLiveData<WebResponse<ListResponse>>()
    val listLiveData: MutableLiveData<WebResponse<ListResponse>> get() = listLiveDataPrivate


    //get List items
    fun getListItems(page: Int) {
        webService = WisdomLeafApplication.getInstance().getWebServiceInstance()
        listLiveDataPrivate.value = WebResponse(Status.LOADING, null, null, true)

        webService.callGetImagesApi(page, 20)
            .enqueue(object : Callback<ListResponse> {
                override fun onResponse(
                    call: Call<ListResponse>,
                    response: Response<ListResponse>
                ) {

                    if (response.isSuccessful && response.body() != null) {
                        val result = response.body()
                        result?.let {
                            listLiveDataPrivate.value = WebResponse(
                                Status.SUCCESS,
                                result,
                                "Success",
                                false
                            )// Resource.Success(data = result)
                        }
                    }
                }

                override fun onFailure(call: Call<ListResponse?>, error: Throwable) {
                    val errorMsg: String? = ErrorHandler.reportError(error)
                    listLiveDataPrivate.value = WebResponse(
                        Status.FAILURE,
                        null,
                        errorMsg,
                        false
                    )

                }
            })
    }


}