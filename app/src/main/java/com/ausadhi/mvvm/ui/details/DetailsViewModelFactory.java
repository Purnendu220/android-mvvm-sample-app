package com.ausadhi.mvvm.ui.details;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.ausadhi.mvvm.data.network.model.Movie;

/**
 * Created by Ali Asadi on 19/12/2018.
 */
public class DetailsViewModelFactory implements ViewModelProvider.Factory {

    private final Movie movie;

    public DetailsViewModelFactory(Movie movie) {
        this.movie = movie;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DetailsViewModel.class)) {
            return (T) new DetailsViewModel(movie);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}
