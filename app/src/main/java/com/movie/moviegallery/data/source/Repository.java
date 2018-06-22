package com.movie.moviegallery.data.source;

import android.support.annotation.NonNull;

import com.movie.moviegallery.data.model.Movie;
import com.movie.moviegallery.data.source.local.LocalDataSource;
import com.movie.moviegallery.data.source.remote.RemoteDataSource;

import java.util.List;

public class Repository implements RepositoryDataSource {
    private static Repository INSTANCE = null;
    private final RemoteDataSource remoteDataSource;
    private final LocalDataSource localDataSource;

    private Repository(@NonNull RemoteDataSource remoteDataSource,
                       @NonNull LocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public static Repository getInstance(@NonNull RemoteDataSource remoteDataSource, @NonNull LocalDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new Repository(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getPopularMovies(IDataSource.LoadDataCallback<Movie> callback, boolean isNetworkAvailable) {
        if (isNetworkAvailable) {
            getPopularMoviesFromRemoteDataSource(callback);
        } else {
            getPopularMoviesFromLocalDataSource(callback);
        }
    }

    /**
     * Get the popular movies from the API
     *
     * @param loadDataCallback The loadDataCallback to handle data loading
     */
    private void getPopularMoviesFromRemoteDataSource(final IDataSource.LoadDataCallback<Movie> loadDataCallback) {
        remoteDataSource.getPopularMovies(new IDataSource.LoadDataCallback<Movie>() {
            @Override
            public void onDataLoaded(List<Movie> movies) {
                refreshPopularMovieLocalDataSource(movies);
                loadDataCallback.onDataLoaded(movies);
            }

            @Override
            public void onDataNotAvailable() {
                loadDataCallback.onDataNotAvailable();
            }
        });
    }

    /**
     * To refresh the local data with the new one once the API is success
     *
     * @param movies The list of movies retrieved from API
     */
    private void refreshPopularMovieLocalDataSource(List<Movie> movies) {
        localDataSource.addPopularMovies(movies);
    }

    /**
     * Get the popular movies from the local database
     *
     * @param loadDataCallback The loadDataCallback to handle data loading
     */
    private void getPopularMoviesFromLocalDataSource(IDataSource.LoadDataCallback<Movie> loadDataCallback) {
        localDataSource.getAllPopularMovies(loadDataCallback);
    }

    @Override
    public void getTopRatedMovies(IDataSource.LoadDataCallback<Movie> loadDataCallback, boolean isNetworkAvailable) {
        if (isNetworkAvailable) {
            getTopRateMoviesFromRemoteDataSource(loadDataCallback);
        } else {
            getTopRatedMoviesFromLocalDataSource(loadDataCallback);
        }
    }

    /**
     * Get the top rated movies from the API
     *
     * @param loadDataCallback The callback to handle data loading
     */
    private void getTopRateMoviesFromRemoteDataSource(final IDataSource.LoadDataCallback<Movie> loadDataCallback) {
        remoteDataSource.getTopRateMovies(new IDataSource.LoadDataCallback<Movie>() {
            @Override
            public void onDataLoaded(List<Movie> movies) {
                refreshTopRatedMovieLocalDataSource(movies);
                loadDataCallback.onDataLoaded(movies);
            }

            @Override
            public void onDataNotAvailable() {
                loadDataCallback.onDataNotAvailable();
            }
        });
    }

    /**
     * To refresh the local data with the new one once the API is success
     *
     * @param movies The list of movies retrieved from API
     */
    private void refreshTopRatedMovieLocalDataSource(List<Movie> movies) {
        localDataSource.addTopRatedMovies(movies);
    }

    /**
     * Get the top rated movies from the local database
     *
     * @param loadDataCallback The loadDataCallback to handle data loading
     */
    private void getTopRatedMoviesFromLocalDataSource(IDataSource.LoadDataCallback<Movie> loadDataCallback) {
        localDataSource.getAllTopRatedMovies(loadDataCallback);
    }

    @Override
    public void getMovieDetail(IDataSource.LoadMovieDetailCallback<Movie> loadMovieDetailCallback, int movieId, int selectedMovieType) {
        localDataSource.getMovieDetailById(loadMovieDetailCallback, movieId, selectedMovieType);
    }

    @Override
    public void markAsFavorite(IDataSource.MarkAsFavoriteCallback markAsFavoriteCallback, int movieId, int selectedMovieType) {
        localDataSource.markAsFavorite(markAsFavoriteCallback, movieId, selectedMovieType);
    }
}