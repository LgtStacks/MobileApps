package edu.uw.main.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class UserInfoViewModel extends ViewModel {
    public String getEmail() {
        return mEmail;
    }
    public String getJwt() {return mJwt; }
    private UserInfoViewModel(String email, String jwt) {
        mEmail = email;
        mJwt = jwt;
    }
   // private UserInfoViewModel(String jwt) { mJwt = jwt;}

    private String mEmail;
    private String mJwt;
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
