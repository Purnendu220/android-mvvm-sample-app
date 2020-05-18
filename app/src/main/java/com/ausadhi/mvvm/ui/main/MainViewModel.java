package com.ausadhi.mvvm.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.ausadhi.mvvm.data.network.model.Movie;
import com.ausadhi.mvvm.data.network.model.MovieResponse;
import com.ausadhi.mvvm.data.network.services.MovieService;
import com.ausadhi.mvvm.ui.base.BaseViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Purnendu on 18/12/2020.
 */
public class MainViewModel extends BaseViewModel {

    private MutableLiveData<List<Movie>> movies;
    private MutableLiveData<Boolean> isLoading;

    private MovieService movieService;

    MainViewModel(MovieService movieService) {
        this.movieService = movieService;
        movies = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
    }

    MutableLiveData<List<Movie>> getMovies() {
        return movies;
    }

    MutableLiveData<Boolean> getLoadingStatus() {
        return isLoading;
    }

    void loadMoviesNetwork() {
        setIsLoading(true);

        movieService.getMovieApi().getAllMovie().enqueue(new MovieCallback());
    }
    void loadMovieLocal() {
        setIsLoading(true);

        setMovies(createLocalMovieList());
    }
    void onEmptyClicked() { setMovies(Collections.<Movie>emptyList()); }

    private List<Movie> createLocalMovieList() {
        String name = "Breaking Bad";
        String image = "https://coderwall-assets-0.s3.amazonaws.com/" +
                "uploads/picture/file/622/breaking_bad_css3_svg_raw.png";

        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie(name,image,name));
        movies.add(new Movie(name,image,name));
        movies.add(new Movie(name,image,name));
        return movies;
    }
    private void setIsLoading(boolean loading) {
        isLoading.postValue(loading);
    }
    private void setMovies(List<Movie> movies) {
        setIsLoading(false);
        this.movies.postValue(movies);
    }

    /**
     * Callback
     **/
    private class MovieCallback implements Callback<MovieResponse> {

        @Override
        public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
            MovieResponse movieResponse = response.body();

            if (movieResponse != null) {
                setMovies(movieResponse.getMovies());
            } else {
                setMovies(Collections.<Movie>emptyList());
            }
        }

        @Override
        public void onFailure(Call<MovieResponse> call, Throwable t) {
            setMovies(Collections.<Movie>emptyList());

        }
    }
}
