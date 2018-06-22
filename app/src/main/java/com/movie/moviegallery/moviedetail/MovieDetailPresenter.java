package com.movie.moviegallery.moviedetail;

import android.support.annotation.NonNull;

import com.movie.moviegallery.data.model.Movie;
import com.movie.moviegallery.data.source.RepositoryDataSource;
import com.movie.moviegallery.data.source.remote.IRemoteDataSource;

public class MovieDetailPresenter implements MovieDetailContract.Presenter {
    private static final String TAG = MovieDetailPresenter.class.getSimpleName();
    private MovieDetailContract.View view;
    private final RepositoryDataSource repository;

    MovieDetailPresenter(@NonNull MovieDetailContract.View view, @NonNull RepositoryDataSource repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void loadMovieDetail(int movieId, int selectedMovieType) {
        loadMovieDetailFromLocalDataSource(movieId, selectedMovieType);
    }

    private void loadMovieDetailFromLocalDataSource(int movieId, int selectedMovieType) {
        if (this.view == null) {
            return;
        }
        this.view.showProgress();
        repository.getMovieDetail(new IRemoteDataSource.LoadMovieDetailCallback<Movie>() {
            @Override
            public void onDataLoaded(Movie movie) {
                MovieDetailPresenter.this.view.hideProgress();
                MovieDetailPresenter.this.view.showMovieDetail(movie);
            }

            @Override
            public void onDataNotAvailable() {
                MovieDetailPresenter.this.view.hideProgress();
                MovieDetailPresenter.this.view.showErrorMessage();
            }
        }, movieId, selectedMovieType);
    }

    @Override
    public void onDestroy() {
        this.view = null;
    }

    @Override
    public void markAsFavorite(int movieId, int selectedMovieType) {
        repository.markAsFavorite(new IRemoteDataSource.MarkAsFavoriteCallback() {
            @Override
            public void onMarkAsFavoriteSuccess() {
                if (MovieDetailPresenter.this.view != null) {
                    MovieDetailPresenter.this.view.showFavoriteSuccess();
                }
            }

            @Override
            public void onMarkAsFavoriteFailure() {
                if (MovieDetailPresenter.this.view != null) {
                    MovieDetailPresenter.this.view.showFavoriteFailure();
                }
            }
        }, movieId, selectedMovieType);
    }
}