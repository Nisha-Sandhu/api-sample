package  com.example.wisdomleafassignment.network

/*
 this class has private constructor
 in order to restrict its object creation
 it contains all the url used for network request
*/
class WebUrl private constructor() {

    companion object {
        // base url
        const val BASE_URL = "https://picsum.photos/"
        const val LIST_API = "v2/list"



    }
}
