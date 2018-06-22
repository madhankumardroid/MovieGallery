package com.movie.moviegallery.gallery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.movie.moviegallery.BuildConfig;
import com.movie.moviegallery.R;
import com.movie.moviegallery.data.model.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ItemHolder> {
    public interface ItemClickListener {
        void onItemClick(Movie movie);
    }

    private final ItemClickListener itemClickListener;
    private List<Movie> movieList;
    private final Context context;

    MovieAdapter(Context context, ItemClickListener itemClickListener, List<Movie> movieList) {
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, int position) {
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(MovieAdapter.this.movieList.get(holder.getAdapterPosition()));
                }
            }
        });
        String imageUrl = BuildConfig.IMAGE_BASE_URL + BuildConfig.IMAGE_SIZE + this.movieList.get(position).getPosterPath();
        Glide.with(context).load(imageUrl).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.movie_reel).error(R.drawable.movie_reel)).into(holder.ivMovie);
    }

    @Override
    public int getItemCount() {
        return this.movieList != null ? movieList.size() : 0;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        private final View view;
        private final ImageView ivMovie;

        ItemHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.ivMovie = view.findViewById(R.id.iv_movie);
        }
    }
}