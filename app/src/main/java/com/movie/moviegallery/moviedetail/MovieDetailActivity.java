package com.movie.moviegallery.moviedetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.movie.moviegallery.BuildConfig;
import com.movie.moviegallery.Injection;
import com.movie.moviegallery.R;
import com.movie.moviegallery.data.model.Movie;

import java.util.Locale;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailContract.View, View.OnClickListener {
    private ProgressBar progressBar;
    private TextView tvTitle;
    private TextView tvReleaseDate;
    private TextView tvRating;
    private TextView tvOverview;
    private ImageView ivThumbnail;
    private Button btnFavorite;
    private int selectedMovieType;
    private MovieDetailPresenter presenter;
    private int movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        initUI();
        initPresenter();
        handleIntent(getIntent());
        btnFavorite.setOnClickListener(this);
    }

    private void initPresenter() {
        presenter = new MovieDetailPresenter(this, Injection.provideRepository(getApplicationContext()));
    }

    private void handleIntent(Intent intent) {
        if (intent != null) {
            movieId = intent.getIntExtra("movie_id", 0);
            selectedMovieType = intent.getIntExtra("movie_type", 0);
            presenter.loadMovieDetail(movieId, selectedMovieType);
        }
    }

    private void initUI() {
        Toolbar toolbar = findViewById(R.id.toolbar_movie_detail);
        setSupportActionBar(toolbar);
        final ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setTitle(R.string.movie_detail);
        }
        progressBar = findViewById(R.id.progressbar_movie_detail);
        tvTitle = findViewById(R.id.tv_title);
        tvReleaseDate = findViewById(R.id.tv_release_date);
        tvRating = findViewById(R.id.tv_rating);
        tvOverview = findViewById(R.id.tv_overview);
        ivThumbnail = findViewById(R.id.iv_thumbnail);
        btnFavorite = findViewById(R.id.btn_favorite);
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
    public void showMovieDetail(Movie movie) {
        tvTitle.setVisibility(View.VISIBLE);
        tvOverview.setVisibility(View.VISIBLE);
        tvRating.setVisibility(View.VISIBLE);
        tvRating.setVisibility(View.VISIBLE);
        ivThumbnail.setVisibility(View.VISIBLE);
        btnFavorite.setVisibility(View.VISIBLE);
        tvReleaseDate.setVisibility(View.VISIBLE);

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        tvRating.setText(String.format(Locale.ENGLISH, "%.1f/10", movie.getVoteAverage()));
        final String releasedOn = getString(R.string.released_on) +" " + movie.getReleaseDate();
        tvReleaseDate.setText(releasedOn);
        if (movie.isFavorite()) {
            disableFavoriteButton();
        } else {
            btnFavorite.setEnabled(true);
            btnFavorite.setAlpha(1.0f);
            btnFavorite.setText(getString(R.string.mark_as_favorite));
        }
        String imageUrl = BuildConfig.IMAGE_BASE_URL + BuildConfig.IMAGE_SIZE + movie.getPosterPath();
        Glide.with(this).load(imageUrl).apply(new RequestOptions().placeholder(R.drawable.movie_reel).error(R.drawable.movie_reel)).into(ivThumbnail);
    }

    private void disableFavoriteButton() {
        btnFavorite.setEnabled(false);
        btnFavorite.setAlpha(0.5f);
        btnFavorite.setText(R.string.favorite);
    }

    @Override
    public void showErrorMessage() {
        Toast.makeText(this, R.string.movie_detail_not_available, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFavoriteSuccess() {
        Toast.makeText(this, R.string.movie_favorite_success, Toast.LENGTH_SHORT).show();
        disableFavoriteButton();
    }

    @Override
    public void showFavoriteFailure() {
        Toast.makeText(this, R.string.movie_favorite_failure, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btn_favorite) {
            presenter.markAsFavorite(movieId, selectedMovieType);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }
}
