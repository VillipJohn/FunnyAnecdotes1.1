package com.example.pc_4.funnyanecdotes11.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by PC-4 on 16.03.2017.
 */

public class Controller implements Callback<List<Anecdote>> {
    static final String BASE_URL = "https://git.eclipse.org/r/";

    public void start() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        GerritAPI gerritAPI = retrofit.create(GerritAPI.class);

        Call<List<Change>> call = gerritAPI.loadChanges("status:open");
        call.enqueue(this);

    }

    @Override
    public void onResponse(Call<List<Anecdote>> call, Response<List<Anecdote>> response) {
        if(response.isSuccessful()) {
            List<Anecdote> changesList = response.body();
            changesList.forEach(change -> System.out.println(change.subject));
        } else {
            System.out.println(response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<List<Anecdote>> call, Throwable t) {
        t.printStackTrace();
    }
}
