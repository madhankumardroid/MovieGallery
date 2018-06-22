package com.movie.moviegallery.data.source.local;

import android.support.annotation.NonNull;

import com.movie.moviegallery.data.model.Movie;
import com.movie.moviegallery.data.source.IDataSource;

import java.util.List;

public interface ILocalDataSource extends IDataSource {
    void getAllPopularMovies(@NonNull LoadDataCallback<Movie> callback);
    void getAllTopRatedMovies(@NonNull LoadDataCallback<Movie> loadDataCallback);
    void getMovieDetailById(LoadMovieDetailCallback<Movie> loadMovieDetailCallback, int movieId, int selectedMovieType);
    void markAsFavorite(MarkAsFavoriteCallback markAsFavoriteCallback, int movieId, int selectedMovieType);
    void addPopularMovies(List<Movie> movies);
    void addTopRatedMovies(List<Movie> movies);
}

