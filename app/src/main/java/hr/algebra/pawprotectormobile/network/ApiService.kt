package hr.algebra.pawprotectormobile.network

import hr.algebra.pawprotectormobile.model.Dog
import hr.algebra.pawprotectormobile.model.TokenResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiService {
    @GET("api/Login/getToken")
    suspend fun getToken(@Query("apiKey") apiKey: String): Response<ResponseBody> // For plain text response


    @GET("get-all-dogs")
    suspend fun getAllDogs(@Header("Authorization") auth: String): Response<List<Dog>>
}
