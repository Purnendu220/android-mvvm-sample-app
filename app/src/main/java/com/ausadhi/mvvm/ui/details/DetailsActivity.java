package com.ausadhi.mvvm.ui.details;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import android.widget.TextView;
import com.ausadhi.mvvm.ui.base.BaseActivity;
import com.bumptech.glide.Glide;
import com.ausadhi.mvvm.R;
import com.ausadhi.mvvm.data.network.model.Movie;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ali Asadi on 12/03/2018.
 */
public class DetailsActivity extends BaseActivity<DetailsViewModel> {

    private static final String EXTRA_MOVIE = "EXTRA_MOVIE";

    @BindView(R.id.image) AppCompatImageView image;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.desc) TextView desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        viewModel.loadMovieData();
        viewModel.getMovie().observe(this, new MovieObserver());
    }

    @NonNull
    @Override
    protected DetailsViewModel createViewModel() {
        Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        DetailsViewModelFactory factory = new DetailsViewModelFactory(movie);
        return ViewModelProviders.of(this,factory).get(DetailsViewModel.class);
    }

    @NonNull
    @Override
    protected void setUpToolbar() {

    }

    public static void start(Context context, Movie movie) {
        Intent starter = new Intent(context, DetailsActivity.class);
        starter.putExtra(EXTRA_MOVIE, movie);
        context.startActivity(starter);
    }

    private class MovieObserver implements Observer<Movie> {
        @Override
        public void onChanged(@Nullable Movie movie) {
            if (movie == null) return;

            title.setText(movie.getTitle());
            desc.setText(movie.getDescription());
            Glide.with(getApplicationContext()).load(movie.getImage()).into(image);
        }
    }

}

