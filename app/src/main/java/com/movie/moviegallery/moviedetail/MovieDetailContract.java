package com.movie.moviegallery.moviedetail;

import com.movie.moviegallery.data.model.Movie;

public interface MovieDetailContract {
    interface View {
        void showProgress();
        void hideProgress();
        void showMovieDetail(Movie movie);
        void showErrorMessage();
        void showFavoriteSuccess();
        void showFavoriteFailure();
    }

    interface Presenter{
        void loadMovieDetail(int movieId, int selectedMovieType);
        void onDestroy();
        void markAsFavorite(int movieId, int selectedMovieType);
    }
}