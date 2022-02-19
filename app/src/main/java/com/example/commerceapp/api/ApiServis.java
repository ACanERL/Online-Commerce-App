package com.example.commerceapp.api;




import com.example.commerceapp.model.Products;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiServis {
    @GET("/v3/0a57ba7d-0124-45fc-861a-caba8eaab2fb")
    Call<List<Products>> getProducts();
}
