package com.ausadhi.mvvm.ui.details;

import androidx.lifecycle.MutableLiveData;

import com.ausadhi.mvvm.data.network.model.Movie;
import com.ausadhi.mvvm.ui.base.BaseViewModel;


/**
 * Created by Ali Asadi on 12/03/2018.
 */
class DetailsViewModel extends BaseViewModel {

    private MutableLiveData<Movie> movieLiveData = new MutableLiveData<>();
    private Movie movie;

    DetailsViewModel(Movie movie) {
        this.movie = movie;
    }

    void loadMovieData() {
        movieLiveData.postValue(movie);
    }

    MutableLiveData<Movie> getMovie() {
        return movieLiveData;
    }
}
