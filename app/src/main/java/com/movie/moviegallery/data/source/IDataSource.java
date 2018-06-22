package com.movie.moviegallery.data.source;

import com.movie.moviegallery.data.model.Movie;

import java.util.List;

public interface IDataSource {
    interface LoadDataCallback<T> {

        void onDataLoaded(List<T> list);

        void onDataNotAvailable();
    }

    interface LoadMovieDetailCallback<T> {
        void onDataLoaded(Movie movie);

        void onDataNotAvailable();
    }

    interface MarkAsFavoriteCallback {
        void onMarkAsFavoriteSuccess();
        void onMarkAsFavoriteFailure();
    }
}
