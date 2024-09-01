package hr.algebra.pawprotectormobile.utils

import hr.algebra.pawprotectormobile.network.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object RetrofitInstance {

    val api: ApiService by lazy{
        Retrofit.Builder()
            .baseUrl(Utils.BASE)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}