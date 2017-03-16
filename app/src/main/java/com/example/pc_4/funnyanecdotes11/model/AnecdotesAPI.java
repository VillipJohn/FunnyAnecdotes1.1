package com.example.pc_4.funnyanecdotes11.model;

import java.util.List;

import retrofit2.Callback;
import retrofit2.http.GET;

/**
 * Created by PC-4 on 16.03.2017.
 */

public interface AnecdotesAPI {

    /*Retrofit get annotation with our URL
       And our method that will return us the list ob Book
    */
    @GET("&fields=id,content.json")
    public void getAnecdotes(Callback<List<Anecdote>> response);

    /*//This method is used for "GET"
    @GET("/api.php")
    Call<Anecdote> get(
            @Query("method") String method,
            @Query("username") String username,
            @Query("password") String password
    );*/
}
