package com.movie.moviegallery.data.source.remote;

import android.support.annotation.NonNull;

import com.movie.moviegallery.data.model.Movie;
import com.movie.moviegallery.data.source.IDataSource;

public interface IRemoteDataSource extends IDataSource {
    void getPopularMovies(@NonNull LoadDataCallback<Movie> callback);

    void getTopRateMovies(LoadDataCallback<Movie> callback);
}