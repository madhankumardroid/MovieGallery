package com.movie.moviegallery.data.source;

import com.movie.moviegallery.data.model.Movie;

public interface RepositoryDataSource {
    void getPopularMovies(IDataSource.LoadDataCallback<Movie> callback, boolean isNetworkAvailable);

    void getTopRatedMovies(IDataSource.LoadDataCallback<Movie> loadDataCallback, boolean isNetworkAvailable);

    void getMovieDetail(IDataSource.LoadMovieDetailCallback<Movie> loadMovieDetailCallback, int movieId, int selectedMovieType);

    void markAsFavorite(IDataSource.MarkAsFavoriteCallback markAsFavoriteCallback, int movieId, int selectedMovieType);
}
