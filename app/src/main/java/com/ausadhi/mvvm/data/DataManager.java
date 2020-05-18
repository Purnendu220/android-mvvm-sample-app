package com.ausadhi.mvvm.data;

import com.ausadhi.mvvm.App;
import com.ausadhi.mvvm.data.db.StorePrefrence;
import com.ausadhi.mvvm.data.db.database.LogDatabase;
import com.ausadhi.mvvm.data.network.services.LoginService;
import com.ausadhi.mvvm.data.network.services.MovieService;
import com.ausadhi.mvvm.data.network.services.SignupService;
import com.preference.PowerPreference;
import com.preference.Preference;

/**
 * Created by Ali Asadi on 26/03/2018.
 */

public class DataManager {

    private static DataManager sInstance;

    private DataManager() {
        // This class is not publicly instantiable
    }

    public static synchronized DataManager getInstance() {
        if (sInstance == null) {
            sInstance = new DataManager();
        }
        return sInstance;
    }

    public StorePrefrence getPrefs() {
        return StorePrefrence.getInstance();
    }

    public LogDatabase getLogDatabse() {
        return LogDatabase.getInstance(App.getInstance());
    }

    public MovieService getMovieService() {
        return MovieService.getInstance();
    }
    public LoginService getLoginService(){
        return LoginService.getInstance();
    }
    public SignupService getSignupService(){
        return SignupService.getInstance();
    }
}
