package com.movie.moviegallery.data.source.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.movie.moviegallery.Constants;
import com.movie.moviegallery.data.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class LocalDataSource implements ILocalDataSource {
    private static LocalDataSource INSTANCE = null;
    private final ContentResolver contentResolver;

    private LocalDataSource(@NonNull ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public static LocalDataSource getInstance(@NonNull ContentResolver contentResolver) {
        if (INSTANCE == null) {
            INSTANCE = new LocalDataSource(contentResolver);
        }
        return INSTANCE;
    }

    @Override
    public void getAllPopularMovies(@NonNull LoadDataCallback<Movie> callback) {
        List<Movie> movies = new ArrayList<>();
        Cursor cursor = contentResolver.query(MoviesContract.PopularMoviesEntry.CONTENT_URI, null,
                null, null, null);
        getDataFromCursor(callback, movies, cursor);
    }

    private void getDataFromCursor(@NonNull LoadDataCallback<Movie> callback, List<Movie> movies, Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Movie movie = getMovie(cursor);
                movies.add(movie);
            }
            cursor.close();
        }

        if (movies.isEmpty()) {
            callback.onDataNotAvailable();
        } else {
            callback.onDataLoaded(movies);
        }
    }

    @Override
    public void getAllTopRatedMovies(@NonNull LoadDataCallback<Movie> loadDataCallback) {
        List<Movie> movies = new ArrayList<>();
        Cursor cursor = contentResolver.query(MoviesContract.TopRatedMoviesEntry.CONTENT_URI, null,
                null, null, null);
        getDataFromCursor(loadDataCallback, movies, cursor);
    }

    @Override
    public void getMovieDetailById(LoadMovieDetailCallback<Movie> loadMovieDetailCallback, int movieId, int selectedMovieType) {
        Movie movie = null;
        Uri uri = null;
        if (selectedMovieType == Constants.POPULAR_MOVIE) {
            uri = MoviesContract.PopularMoviesEntry.CONTENT_URI.buildUpon().appendPath("movie_detail").appendEncodedPath(String.valueOf(movieId)).build();
        } else if (selectedMovieType == Constants.TOP_RATED_MOVIE) {
            uri = MoviesContract.TopRatedMoviesEntry.CONTENT_URI.buildUpon().appendPath("movie_detail").appendEncodedPath(String.valueOf(movieId)).build();
        }
        Cursor cursor = contentResolver.query(uri, null, null, new String[]{String.valueOf(movieId)}, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                movie = getMovie(cursor);
            }
            cursor.close();
        }

        if (movie != null) {
            loadMovieDetailCallback.onDataLoaded(movie);
        } else {
            loadMovieDetailCallback.onDataNotAvailable();
        }
    }

    @Override
    public void markAsFavorite(MarkAsFavoriteCallback markAsFavoriteCallback, int movieId, int selectedMovieType) {
        Uri uri = null;
        ContentValues favoriteValues = null;
        if (selectedMovieType == Constants.POPULAR_MOVIE) {
            uri = MoviesContract.PopularMoviesEntry.CONTENT_URI.buildUpon().appendPath("favorite").appendEncodedPath(String.valueOf(movieId)).build();
            favoriteValues = MovieValues.favoriteFromPopularMovies();
        } else if (selectedMovieType == Constants.TOP_RATED_MOVIE) {
            uri = MoviesContract.TopRatedMoviesEntry.CONTENT_URI.buildUpon().appendPath("favorite").appendEncodedPath(String.valueOf(movieId)).build();
            favoriteValues = MovieValues.favoriteFromTopRatedMovies();
        }

        int success = contentResolver.update(uri, favoriteValues, MoviesContract.TableColumns.MOVIE_ID + " =? ", new String[]{String.valueOf(movieId)});
        if (success > 0) {
            markAsFavoriteCallback.onMarkAsFavoriteSuccess();
        } else {
            markAsFavoriteCallback.onMarkAsFavoriteFailure();
        }
    }

    @Override
    public void addPopularMovies(List<Movie> movies) {
        ArrayList<ContentValues> contentValues = new ArrayList<>();
        for (Movie movie : movies) {
            Uri uri = MoviesContract.PopularMoviesEntry.CONTENT_URI.buildUpon().appendPath("update_detail").appendEncodedPath(String.valueOf(movie.getId())).build();
            ContentValues values = getContentValues(movie);
            int success = contentResolver.update(uri, values, MoviesContract.TableColumns.MOVIE_ID + " =? ", new String[]{String.valueOf(movie.getId())});
            if (success <= 0) {
                contentValues.add(MovieValues.from(movie));
            }
        }
        if (contentValues.size() > 0) {
            ContentValues[] insertValues = contentValues.toArray(new ContentValues[contentValues.size()]);
            contentResolver.bulkInsert(MoviesContract.PopularMoviesEntry.CONTENT_URI, insertValues);
        }
    }

    @Override
    public void addTopRatedMovies(List<Movie> movies) {
        ArrayList<ContentValues> contentValues = new ArrayList<>();
        for (Movie movie : movies) {
            Uri uri = MoviesContract.TopRatedMoviesEntry.CONTENT_URI.buildUpon().appendPath("update_detail").appendEncodedPath(String.valueOf(movie.getId())).build();
            ContentValues values = getContentValues(movie);
            int success = contentResolver.update(uri, values, MoviesContract.TableColumns.MOVIE_ID + " =? ", new String[]{String.valueOf(movie.getId())});
            if (success <= 0) {
                contentValues.add(MovieValues.from(movie));
            }
        }
        if (contentValues.size() > 0) {
            ContentValues[] insertValues = contentValues.toArray(new ContentValues[contentValues.size()]);
            contentResolver.bulkInsert(MoviesContract.TopRatedMoviesEntry.CONTENT_URI, insertValues);
        }
    }

    @NonNull
    private Movie getMovie(Cursor cursor) {
        String movieId = cursor.getString(cursor.getColumnIndex(MoviesContract.TableColumns.MOVIE_ID));
        String movieTitle = cursor.getString(cursor.getColumnIndex(MoviesContract.TableColumns.TITLE));
        double movieVoteAverage = cursor.getDouble(cursor.getColumnIndex(MoviesContract.TableColumns.VOTE_AVERAGE));
        String moviePosterPath = cursor.getString(cursor.getColumnIndex(MoviesContract.TableColumns.POSTER_PATH));
        String movieOriginalTitle = cursor.getString(cursor.getColumnIndex(MoviesContract.TableColumns.ORIGINAL_TITLE));
        String movieOverview = cursor.getString(cursor.getColumnIndex(MoviesContract.TableColumns.OVERVIEW));
        String movieReleaseDate = cursor.getString(cursor.getColumnIndex(MoviesContract.TableColumns.RELEASE_DATE));
        boolean isFavorite = cursor.getInt(cursor.getColumnIndex(MoviesContract.TableColumns.FAVORITE)) == 1;
        return new Movie(Integer.parseInt(movieId), movieTitle, movieVoteAverage,
                moviePosterPath, movieOriginalTitle, movieOverview,
                movieReleaseDate, isFavorite);
    }

    @NonNull
    private ContentValues getContentValues(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MoviesContract.TopRatedMoviesEntry.MOVIE_ID, movie.getId());
        values.put(MoviesContract.TopRatedMoviesEntry.TITLE, movie.getTitle());
        values.put(MoviesContract.TopRatedMoviesEntry.OVERVIEW, movie.getOverview());
        values.put(MoviesContract.TopRatedMoviesEntry.ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(MoviesContract.TopRatedMoviesEntry.RELEASE_DATE, movie.getReleaseDate());
        values.put(MoviesContract.TopRatedMoviesEntry.POSTER_PATH, movie.getPosterPath());
        values.put(MoviesContract.TopRatedMoviesEntry.VOTE_AVERAGE, movie.getVoteAverage());
        return values;
    }
}