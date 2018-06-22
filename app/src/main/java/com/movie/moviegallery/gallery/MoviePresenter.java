package com.movie.moviegallery.gallery;

import android.support.annotation.NonNull;
import android.util.Log;

import com.movie.moviegallery.data.model.Movie;
import com.movie.moviegallery.data.source.IDataSource;
import com.movie.moviegallery.data.source.RepositoryDataSource;
import com.movie.moviegallery.data.source.remote.IRemoteDataSource;

import java.util.List;

public class MoviePresenter implements MovieGalleryContract.Presenter {
    private static final String TAG = MoviePresenter.class.getSimpleName();
    private MovieGalleryContract.View view;
    private RepositoryDataSource repository;
    private final IDataSource.LoadDataCallback<Movie> loadDataCallback = new IRemoteDataSource.LoadDataCallback<Movie>() {
        @Override
        public void onDataLoaded(List<Movie> list) {
            if (view != null) {
                if (list == null || list.isEmpty()) {
                    view.showErrorMessage();
                } else {
                    view.showMovies(list);
                }
                view.hideProgress();
            } else {
                Log.d(TAG, "onDataLoaded: View is null");
            }
        }

        @Override
        public void onDataNotAvailable() {
            if (view != null) {
                view.hideProgress();
                view.showErrorMessage();
            } else {
                Log.d(TAG, "onDataNotAvailable: View is null");
            }
        }
    };

    MoviePresenter(@NonNull RepositoryDataSource repository, @NonNull MovieGalleryContract.View view) {
        this.repository = repository;
        this.view = view;
    }

    private boolean isNetworkAvailable() {
        return this.view != null && this.view.isNetworkAvailable();
    }

    @Override
    public void loadPopularMovies() {
        loadPopularMoviesFromRepository(isNetworkAvailable());
    }


    @Override
    public void loadTopRatedMovies() {
        loadTopRatedMoviesFromRepository(isNetworkAvailable());
    }

    /**
     * Load the top rated movies from the repository
     *
     * @param isNetworkAvailable Whether network is available or not
     */
    private void loadTopRatedMoviesFromRepository(boolean isNetworkAvailable) {
        if (this.view != null) {
            this.view.showProgress();
        }
        repository.getTopRatedMovies(loadDataCallback, isNetworkAvailable);
    }

    /**
     * Load the popular movies from the repository
     *
     * @param isNetworkAvailable Whether network is available or not
     */
    private void loadPopularMoviesFromRepository(boolean isNetworkAvailable) {
        if (this.view != null) {
            this.view.showProgress();
        }
        repository.getPopularMovies(loadDataCallback, isNetworkAvailable);
    }

    @Override
    public void onDestroy() {
        this.view = null;
    }
}
