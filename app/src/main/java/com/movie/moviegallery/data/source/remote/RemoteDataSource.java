package com.movie.moviegallery.data.source.remote;

import android.support.annotation.NonNull;

import com.movie.moviegallery.BuildConfig;
import com.movie.moviegallery.data.model.Movie;
import com.movie.moviegallery.data.model.MoviesResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteDataSource implements IRemoteDataSource {
    private static RemoteDataSource INSTANCE;
    private final ApiService apiService;

    private RemoteDataSource(ApiService apiService) {
        this.apiService = apiService;
    }

    public static RemoteDataSource getInstance(ApiService apiService) {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource(apiService);
        }
        return INSTANCE;
    }

    @Override
    public void getPopularMovies(@NonNull final LoadDataCallback<Movie> callback) {
        Call<MoviesResponse> popularMovies = apiService.getPopularMovies(BuildConfig.MOVIE_DB_API_KEY);
        popularMovies.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                handleResponse(response, callback);
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getTopRateMovies(final LoadDataCallback<Movie> callback) {
        Call<MoviesResponse> topRatedMovies = apiService.getTopRatedMovies(BuildConfig.MOVIE_DB_API_KEY);
        topRatedMovies.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                handleResponse(response, callback);
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    /**
     * Handle the response from the movies api
     *
     * @param response The response object
     * @param callback The callback to inform the availability of data
     */
    private void handleResponse(Response<MoviesResponse> response, @NonNull LoadDataCallback<Movie> callback) {
        if (response.isSuccessful()) {
            final MoviesResponse responseBody = response.body();
            if (responseBody != null && !responseBody.getMovies().isEmpty()) {
                callback.onDataLoaded(responseBody.getMovies());
            } else {
                callback.onDataNotAvailable();
            }
        } else {
            callback.onDataNotAvailable();
        }
    }
}