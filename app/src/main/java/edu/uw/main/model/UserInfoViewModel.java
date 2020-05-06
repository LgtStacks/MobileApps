package edu.uw.main.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * This class will act as a middle ground between the app and the server.
 * @author Group 3
 * @version 5/5
 */
public class UserInfoViewModel extends ViewModel {
    /** The user email. */
    private String mEmail;

    /** The Json web token. */
    private String mJwt;
    /**
     * Constructor for the user info view model.
     * @param email - Email user entered.
     * @param jwt -  Json web token returned from web service.
     */
    private UserInfoViewModel(String email, String jwt) {
        mEmail = email;
        mJwt = jwt;
    }
    /**
     * Gets user email.
     * @return - The Email the user entered.
     */
    public String getEmail() {
        return mEmail;
    }

    /**
     * Gets the JSON WEB TOKEN that is returned when signing in.
     * @return - The JSON WEB TOKEN
     */
    public String getJwt() {return mJwt; }

    /**
     * Inner class to create our user info view model.
     */
    public static class UserInfoViewModelFactory implements ViewModelProvider.Factory {
        private final String email;
        private final String jwt;
        public UserInfoViewModelFactory(String email, String jwt) {
            this.email = email;
            this.jwt = jwt;
        }
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == UserInfoViewModel.class) {
                return (T) new UserInfoViewModel(email, jwt);
            }
            throw new IllegalArgumentException(
                    "Argument must be: " + UserInfoViewModel.class);
        }
    }

}
