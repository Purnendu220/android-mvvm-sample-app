package com.ausadhi.mvvm.ui.main;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ausadhi.mvvm.R;
import com.ausadhi.mvvm.data.DataManager;
import com.ausadhi.mvvm.data.network.model.Movie;
import com.ausadhi.mvvm.ui.base.BaseActivity;
import com.ausadhi.mvvm.ui.details.DetailsActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Purnendu on 12/03/2020.
 */

public class MainActivity extends BaseActivity<MainViewModel> implements MovieAdapter.MovieListener {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.empty_view) TextView emptyView;

    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        movieAdapter = new MovieAdapter(this);
        recyclerView.setAdapter(movieAdapter);

        viewModel.getLoadingStatus().observe(this, new LoadingObserver());
        viewModel.getMovies().observe(this, new MovieObserver());
    }

    @NonNull
    @Override
    protected MainViewModel createViewModel() {
        MainViewModelFactory factory = new MainViewModelFactory(DataManager.getInstance().getMovieService());
        return ViewModelProviders.of(this, factory).get(MainViewModel.class);
    }

    @NonNull
    @Override
    protected void setUpToolbar() {

    }

    @OnClick(R.id.network)
    void onNetworkButtonClick() {
        viewModel.loadMoviesNetwork();
    }

    @OnClick(R.id.local)
    void onLocalButtonClick() {
        viewModel.loadMovieLocal();
    }

    @OnClick(R.id.empty)
    void onEmptyButtonClick() {
        viewModel.onEmptyClicked();
    }

    @Override
    public void onMovieClicked(Movie movie) {
        DetailsActivity.start(this, movie);
    }

    //Observer
    private class LoadingObserver implements Observer<Boolean> {

        @Override
        public void onChanged(@Nullable Boolean isLoading) {
            if (isLoading == null) return;

            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    private class MovieObserver implements Observer<List<Movie>> {

        @Override
        public void onChanged(@Nullable List<Movie> movies) {
            if (movies == null) return;
            movieAdapter.setItems(movies);

            if (movies.isEmpty()) {
                emptyView.setVisibility(View.VISIBLE);
            } else {
                emptyView.setVisibility(View.GONE);
            }
        }
    }
}
