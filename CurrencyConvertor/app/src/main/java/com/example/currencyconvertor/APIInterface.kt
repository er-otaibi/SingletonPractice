package com.example.currencyconvertor

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface APIInterface {

    @GET("eur.json")
//    https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/eur.json
    fun getCurrency(): Call<CurrencyDetails.Datum>?
}