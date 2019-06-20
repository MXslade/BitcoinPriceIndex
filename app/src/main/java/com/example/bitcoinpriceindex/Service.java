package com.example.bitcoinpriceindex;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Service {

    @GET("/api/v2/transactions/btcusd/")
    Call<JsonArray> readTransactionsArray();

}
