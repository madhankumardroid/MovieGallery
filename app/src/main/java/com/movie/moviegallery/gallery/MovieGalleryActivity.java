package com.movie.moviegallery.gallery;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.movie.moviegallery.Injection;
import com.movie.moviegallery.R;
import com.movie.moviegallery.data.model.Movie;
import com.movie.moviegallery.moviedetail.MovieDetailActivity;

import java.util.ArrayList;
import java.util.List;

import static com.movie.moviegallery.Constants.POPULAR_MOVIE;
import static com.movie.moviegallery.Constants.TOP_RATED_MOVIE;

public class MovieGalleryActivity extends AppCompatActivity implements MovieGalleryContract.View, View.OnClickListener, MovieAdapter.ItemClickListener {
    private MovieAdapter movieAdapter;
    private ProgressBar progressBar;
    private MovieGalleryContract.Presenter presenter;
    private int selectedMovieType;
    private LinearLayout llNoNetwork;
    private RecyclerView rvMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        initViews();
        Button btnRetry = findViewById(R.id.btn_retry);
        btnRetry.setOnClickListener(this);
        initToolbar();
        initRecyclerView();
        initPresenter();
        selectedMovieType = POPULAR_MOVIE;
        presenter.loadPopularMovies();
    }

    private void initViews() {
        progressBar = findViewById(R.id.progressbar_movie);
        llNoNetwork = findViewById(R.id.ll_no_network);
        rvMovies = findViewById(R.id.rv_movies);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.menu_movie_sort_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_popular:
                selectedMovieType = POPULAR_MOVIE;
                presenter.loadPopularMovies();
                break;
            case R.id.menu_top_rated:
                selectedMovieType = TOP_RATED_MOVIE;
                presenter.loadTopRatedMovies();
                break;
        }
        return true;
    }

    private void initPresenter() {
        presenter = new MoviePresenter(Injection.provideRepository(getApplicationContext()), this);
    }

    private void initRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        rvMovies.setLayoutManager(layoutManager);
        rvMovies.setItemAnimator(itemAnimator);
        movieAdapter = new MovieAdapter(this, this, new ArrayList<Movie>());
        rvMovies.setAdapter(movieAdapter);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(R.string.pop_movies);
        }
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showMovies(List<Movie> movies) {
        llNoNetwork.setVisibility(View.GONE);
        if (movieAdapter != null) {
            if (rvMovies.getVisibility() == View.GONE) {
                rvMovies.setVisibility(View.VISIBLE);
            }
            movieAdapter.setMovieList(movies);
        }
    }

    @Override
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void showErrorMessage() {
        if (!isNetworkAvailable()) {
            showNoNetworkView();
        } else {
            Toast.makeText(this, R.string.api_error_occurred, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showNoNetworkView() {
        hideProgress();
        rvMovies.setVisibility(View.GONE);
        llNoNetwork.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btn_retry) {
            if (selectedMovieType == POPULAR_MOVIE) {
                presenter.loadPopularMovies();
            } else if (selectedMovieType == TOP_RATED_MOVIE) {
                presenter.loadTopRatedMovies();
            }
        }
    }

    @Override
    public void onItemClick(Movie movie) {
        Intent detailIntent = new Intent(MovieGalleryActivity.this, MovieDetailActivity.class);
        detailIntent.putExtra("movie_id", movie.getId());
        detailIntent.putExtra("movie_type", selectedMovieType);
        startActivity(detailIntent);
    }
}