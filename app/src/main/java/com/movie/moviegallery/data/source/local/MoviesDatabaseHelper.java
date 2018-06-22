package com.movie.moviegallery.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MoviesDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;
    private final String NOT_NULL_CONSTRAINT = " TEXT NOT NULL DEFAULT '', ";
    private final String CREATE_POPULAR_MOVIES_TABLE = "CREATE TABLE " +
            MoviesContract.PopularMoviesEntry.TABLE_NAME + " (" +
            MoviesContract.PopularMoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MoviesContract.PopularMoviesEntry.MOVIE_ID + NOT_NULL_CONSTRAINT +
            MoviesContract.PopularMoviesEntry.TITLE + NOT_NULL_CONSTRAINT +
            MoviesContract.PopularMoviesEntry.VOTE_AVERAGE + " REAL, " +
            MoviesContract.PopularMoviesEntry.POSTER_PATH + NOT_NULL_CONSTRAINT +
            MoviesContract.PopularMoviesEntry.ORIGINAL_TITLE + NOT_NULL_CONSTRAINT +
            MoviesContract.PopularMoviesEntry.OVERVIEW + NOT_NULL_CONSTRAINT +
            MoviesContract.PopularMoviesEntry.RELEASE_DATE + NOT_NULL_CONSTRAINT +
            MoviesContract.PopularMoviesEntry.FAVORITE + " INTEGER DEFAULT 0 ," +
            " UNIQUE (" + MoviesContract.PopularMoviesEntry.MOVIE_ID + ") ON CONFLICT REPLACE);";

    private final String CREATE_TOP_RATED_MOVIES_TABLE = "CREATE TABLE " +
            MoviesContract.TopRatedMoviesEntry.TABLE_NAME + " (" +
            MoviesContract.TopRatedMoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MoviesContract.TopRatedMoviesEntry.MOVIE_ID + NOT_NULL_CONSTRAINT +
            MoviesContract.TopRatedMoviesEntry.TITLE + NOT_NULL_CONSTRAINT +
            MoviesContract.TopRatedMoviesEntry.VOTE_AVERAGE + " REAL, " +
            MoviesContract.TopRatedMoviesEntry.POSTER_PATH + NOT_NULL_CONSTRAINT +
            MoviesContract.TopRatedMoviesEntry.ORIGINAL_TITLE + NOT_NULL_CONSTRAINT +
            MoviesContract.TopRatedMoviesEntry.OVERVIEW + NOT_NULL_CONSTRAINT +
            MoviesContract.TopRatedMoviesEntry.RELEASE_DATE + NOT_NULL_CONSTRAINT +
            MoviesContract.TopRatedMoviesEntry.FAVORITE + " INTEGER DEFAULT 0 ," +
            " UNIQUE (" + MoviesContract.TopRatedMoviesEntry.MOVIE_ID + ") ON CONFLICT REPLACE);";

    public MoviesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_POPULAR_MOVIES_TABLE);
        db.execSQL(CREATE_TOP_RATED_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}