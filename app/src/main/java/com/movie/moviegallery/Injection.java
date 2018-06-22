package com.movie.moviegallery;

import android.content.Context;
import android.support.annotation.NonNull;

import com.movie.moviegallery.data.source.Repository;
import com.movie.moviegallery.data.source.local.LocalDataSource;
import com.movie.moviegallery.data.source.remote.ApiClient;
import com.movie.moviegallery.data.source.remote.ApiService;
import com.movie.moviegallery.data.source.remote.RemoteDataSource;

public class Injection {

    public static Repository provideRepository(@NonNull Context context) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        return Repository.getInstance(RemoteDataSource.getInstance(apiService), LocalDataSource.getInstance(context.getContentResolver()));
    }
}