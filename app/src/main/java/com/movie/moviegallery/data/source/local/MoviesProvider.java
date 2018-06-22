package com.movie.moviegallery.data.source.local;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MoviesProvider extends ContentProvider {
    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDatabaseHelper openHelper;

    private static final int POPULAR_MOVIES = 1000;
    private static final int POPULAR_MOVIES_WITH_ID = 1001;
    private static final int TOP_RATED_MOVIES = 1002;
    private static final int TOP_RATED_MOVIES_WITH_ID = 1003;
    private static final int GET_POPULAR_MOVIE_DETAIL = 1004;
    private static final int GET_TOP_RATED_MOVIE_DETAIL = 1005;
    private static final int MARK_POPULAR_MOVIE_AS_FAVORITE = 1006;
    private static final int MARK_TOP_RATED_MOVIE_AS_FAVORITE = 1007;
    private static final int UPDATE_POPULAR_MOVIES_WITH_NEW_DETAILS = 1008;
    private static final int UPDATE_TOP_RATED_MOVIES_WITH_NEW_DETAILS = 1009;

    private static final SQLiteQueryBuilder sPopularMovieByIdQueryBuilder;
    private static final SQLiteQueryBuilder sTopRatedMovieByIdQueryBuilder;

    static {
        sPopularMovieByIdQueryBuilder = new SQLiteQueryBuilder();
        sPopularMovieByIdQueryBuilder.setTables(MoviesContract.PopularMoviesEntry.TABLE_NAME);
        sTopRatedMovieByIdQueryBuilder = new SQLiteQueryBuilder();
        sTopRatedMovieByIdQueryBuilder.setTables(MoviesContract.TopRatedMoviesEntry.TABLE_NAME);
    }

    private static final String popularMovieByIdSelection =
            MoviesContract.PopularMoviesEntry.TABLE_NAME + "." + MoviesContract.PopularMoviesEntry._ID + " = ? ";
    private static final String topRatedMovieByIdSelection =
            MoviesContract.PopularMoviesEntry.TABLE_NAME + "." + MoviesContract.TopRatedMoviesEntry._ID + " = ? ";


    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;
        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, MoviesContract.PATH_POPULAR_MOVIES, POPULAR_MOVIES);
        matcher.addURI(authority, MoviesContract.PATH_POPULAR_MOVIES + "/#", POPULAR_MOVIES_WITH_ID);
        matcher.addURI(authority, MoviesContract.PATH_TOP_RATED_MOVIES, TOP_RATED_MOVIES);
        matcher.addURI(authority, MoviesContract.PATH_TOP_RATED_MOVIES + "/#", TOP_RATED_MOVIES_WITH_ID);
        matcher.addURI(authority, MoviesContract.PATH_POPULAR_MOVIES + "/movie_detail/*", GET_POPULAR_MOVIE_DETAIL);
        matcher.addURI(authority, MoviesContract.PATH_TOP_RATED_MOVIES + "/movie_detail/*", GET_TOP_RATED_MOVIE_DETAIL);
        matcher.addURI(authority, MoviesContract.PATH_POPULAR_MOVIES + "/favorite/*", MARK_POPULAR_MOVIE_AS_FAVORITE);
        matcher.addURI(authority, MoviesContract.PATH_TOP_RATED_MOVIES + "/favorite/*", MARK_TOP_RATED_MOVIE_AS_FAVORITE);
        matcher.addURI(authority, MoviesContract.PATH_POPULAR_MOVIES + "/update_detail/*", UPDATE_POPULAR_MOVIES_WITH_NEW_DETAILS);
        matcher.addURI(authority, MoviesContract.PATH_TOP_RATED_MOVIES + "/update_detail/*", UPDATE_TOP_RATED_MOVIES_WITH_NEW_DETAILS);
        return matcher;
    }

    public MoviesProvider() {
    }

    private Cursor getPopularMovieById(Uri uri, String[] projection, String sortOrder) {
        String selectionId = String.valueOf(MoviesContract.PopularMoviesEntry.getIdFromUri(uri));
        String[] selectionArgs = new String[]{selectionId};
        return sPopularMovieByIdQueryBuilder.query(openHelper.getReadableDatabase(),
                projection,
                popularMovieByIdSelection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    private Cursor getPopularMovieDetailById(Uri uri, String[] selectionArgs) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MoviesContract.PopularMoviesEntry.TABLE_NAME);
        final SQLiteDatabase readableDatabase = openHelper.getReadableDatabase();
        queryBuilder.appendWhere(MoviesContract.PopularMoviesEntry.MOVIE_ID + " = '" + uri.getLastPathSegment() + "'");
        return queryBuilder.query(readableDatabase,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    private Cursor getTopRatedMovieDetailById(Uri uri, String[] selectionArgs) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MoviesContract.TopRatedMoviesEntry.TABLE_NAME);
        final SQLiteDatabase readableDatabase = openHelper.getReadableDatabase();
        queryBuilder.appendWhere(MoviesContract.TopRatedMoviesEntry.MOVIE_ID + " = '" + uri.getLastPathSegment() + "'");
        return queryBuilder.query(readableDatabase,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    private Cursor getTopRatedMovieById(Uri uri, String[] projection, String sortOrder) {
        String selectionId = String.valueOf(MoviesContract.TopRatedMoviesEntry.getIdFromUri(uri));
        String[] selectionArgs = new String[]{selectionId};
        return sTopRatedMovieByIdQueryBuilder.query(openHelper.getReadableDatabase(),
                projection,
                topRatedMovieByIdSelection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    @Override
    public boolean onCreate() {
        openHelper = new MoviesDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case POPULAR_MOVIES: {
                retCursor = openHelper.getReadableDatabase().query(
                        MoviesContract.PopularMoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case POPULAR_MOVIES_WITH_ID: {
                retCursor = getPopularMovieById(uri, projection, sortOrder);
                break;
            }
            case TOP_RATED_MOVIES: {
                retCursor = openHelper.getReadableDatabase().query(
                        MoviesContract.TopRatedMoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case TOP_RATED_MOVIES_WITH_ID: {
                retCursor = getTopRatedMovieById(uri, projection, sortOrder);
                break;
            }
            case GET_POPULAR_MOVIE_DETAIL: {
                retCursor = getPopularMovieDetailById(uri, selectionArgs);
                break;
            }
            case GET_TOP_RATED_MOVIE_DETAIL: {
                retCursor = getTopRatedMovieDetailById(uri, selectionArgs);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (getContext() != null) {
            retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case POPULAR_MOVIES:
                return MoviesContract.PopularMoviesEntry.CONTENT_TYPE;
            case POPULAR_MOVIES_WITH_ID:
                return MoviesContract.PopularMoviesEntry.CONTENT_ITEM_TYPE;
            case TOP_RATED_MOVIES:
                return MoviesContract.TopRatedMoviesEntry.CONTENT_TYPE;
            case TOP_RATED_MOVIES_WITH_ID:
                return MoviesContract.TopRatedMoviesEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = openHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case POPULAR_MOVIES: {
                long _id = db.insert(MoviesContract.PopularMoviesEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = MoviesContract.PopularMoviesEntry.buildMovieUri(_id);
                } else {
                    throw new SQLException("Failed to insert row into: " + uri);
                }
                break;
            }
            case TOP_RATED_MOVIES: {
                long _id = db.insert(MoviesContract.TopRatedMoviesEntry.TABLE_NAME, null, values);
                if ((_id > 0)) {
                    returnUri = MoviesContract.TopRatedMoviesEntry.buildMovieUri(_id);
                } else {
                    throw new SQLException("Failed to insert row into: " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = openHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case POPULAR_MOVIES:
                rowsDeleted = db.delete(MoviesContract.PopularMoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TOP_RATED_MOVIES:
                rowsDeleted = db.delete(MoviesContract.TopRatedMoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        if (rowsDeleted != 0) {
            if (getContext() != null) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = openHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case POPULAR_MOVIES:
                rowsUpdated = db.update(MoviesContract.PopularMoviesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case TOP_RATED_MOVIES:
                rowsUpdated = db.update(MoviesContract.TopRatedMoviesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case MARK_POPULAR_MOVIE_AS_FAVORITE:
                rowsUpdated = db.update(MoviesContract.PopularMoviesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case MARK_TOP_RATED_MOVIE_AS_FAVORITE:
                rowsUpdated = db.update(MoviesContract.TopRatedMoviesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case UPDATE_POPULAR_MOVIES_WITH_NEW_DETAILS:
                rowsUpdated = db.update(MoviesContract.PopularMoviesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case UPDATE_TOP_RATED_MOVIES_WITH_NEW_DETAILS:
                rowsUpdated = db.update(MoviesContract.TopRatedMoviesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            if (getContext() != null) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = openHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount = 0;
        switch (match) {
            case POPULAR_MOVIES:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesContract.PopularMoviesEntry.TABLE_NAME,
                                null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (getContext() != null) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return returnCount;
            case TOP_RATED_MOVIES:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesContract.TopRatedMoviesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (getContext() != null) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return returnCount;

            default:
                return super.bulkInsert(uri, values);
        }
    }
}
