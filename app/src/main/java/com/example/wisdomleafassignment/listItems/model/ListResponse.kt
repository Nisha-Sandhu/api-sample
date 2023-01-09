package com.example.wisdomleafassignment.listItems.model

class ListResponse : ArrayList<ListResponse.ListResponseItem>(){
    class ListResponseItem(
        val author: String,
        val download_url: String,
        val height: Int,
        val id: String,
        val url: String,
        val width: Int
    )
}