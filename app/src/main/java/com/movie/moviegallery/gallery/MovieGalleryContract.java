package com.movie.moviegallery.gallery;

import com.movie.moviegallery.data.model.Movie;

import java.util.List;

public interface MovieGalleryContract {
    interface  View {
        void showProgress();
        void hideProgress();
        void showMovies(List<Movie> movies);
        boolean isNetworkAvailable();
        void showErrorMessage();
        void showNoNetworkView();
    }

    interface Presenter {
        void loadPopularMovies();
        void loadTopRatedMovies();
        void onDestroy();
    }
}