package com.movie.moviegallery.data.source.local;

import android.content.ContentValues;

import com.movie.moviegallery.data.model.Movie;

public class MovieValues {
    public static ContentValues from(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MoviesContract.PopularMoviesEntry.MOVIE_ID, movie.getId());
        values.put(MoviesContract.PopularMoviesEntry.TITLE, movie.getTitle());
        values.put(MoviesContract.PopularMoviesEntry.VOTE_AVERAGE, movie.getVoteAverage());
        values.put(MoviesContract.PopularMoviesEntry.POSTER_PATH, movie.getPosterPath());
        values.put(MoviesContract.PopularMoviesEntry.ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(MoviesContract.PopularMoviesEntry.OVERVIEW, movie.getOverview());
        values.put(MoviesContract.PopularMoviesEntry.RELEASE_DATE, movie.getReleaseDate());
        return values;
    }

    public static ContentValues favoriteFromPopularMovies() {
        ContentValues value = new ContentValues();
        value.put(MoviesContract.PopularMoviesEntry.FAVORITE, 1);
        return value;
    }
    public static ContentValues favoriteFromTopRatedMovies() {
        ContentValues value = new ContentValues();
        value.put(MoviesContract.TopRatedMoviesEntry.FAVORITE, 1);
        return value;
    }
}