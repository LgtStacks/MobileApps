package edu.uw.main.ui.recovery;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import edu.uw.main.AuthActivity;
import edu.uw.main.MainActivity;
import edu.uw.main.PasswordValidator;
import edu.uw.main.R;
import edu.uw.main.databinding.FragmentChangeBinding;
import edu.uw.main.model.UserInfoViewModel;
import edu.uw.main.ui.auth.Login.LoginViewModel;
import edu.uw.main.ui.weather.WeatherViewModel;

import static edu.uw.main.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.main.PasswordValidator.checkPwdLength;
import static edu.uw.main.PasswordValidator.checkPwdSpecialChar;

/**
 * A simple {@link Fragment} subclass.
 */
public class change extends Fragment {

    private FragmentChangeBinding  binding;

    private ChangePasswordViewModel mModel;

    private ChangePasswordViewModel mChangeModel;
    private UserInfoViewModel mUserViewModel;

    public change() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChangeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity())
                .get(ChangePasswordViewModel.class);

        mChangeModel = new ViewModelProvider(getActivity())
                .get(ChangePasswordViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).setActionBarTitle("Change Password");
        UserInfoViewModel model = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

        mChangeModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeSignInResponse);

        binding.buttonChange.setOnClickListener(button -> navigateBack(model));
    }

    /**
     * On success, navigates the user back to home screen.
     * Returns error on incorrect password formatting and matching.
     * @param model the UserInfoViewModel
     */
    private void navigateBack(UserInfoViewModel model) {

        if (!binding.textNewPw.getText().toString().equals(binding.textNewRetype.getText().toString())) {
            binding.textNewPw.setError("Passwords do not match");
        } else if (binding.textNewPw.length() < 1) {
            binding.textNewPw.setError("Password field is empty");
        } else {
            mModel.connect(binding.textNewPw.getText().toString(), binding.textOldPw.getText().toString(), model.getmJwt());
        }
    }

    /**
     * Method to inform user that they have entered their original password incorrectly.
     */
    private void processToastSuccess() {
        Toast toast = Toast.makeText(getActivity(), "Password has been successfully changed" , Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }


    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to ChangePasswordViewModel.
     *
     * @param response the Response from the server
     */
    private void observeSignInResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.textOldPw.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));

                } catch (JSONException e) {
                    Log.e("JSON1 Parse Error", e.getMessage());
                    if (e.getMessage().contains("Passwords")) {
                        binding.textOldPw.setError("Your original password is incorrect");

                    } else if (e.getMessage().contains("changed")){
                        processToastSuccess();
                    }
                }
            } else {
                try {
                    mUserViewModel = new ViewModelProvider(getActivity(),
                            new UserInfoViewModel.UserInfoViewModelFactory(
                                    binding.textOldPw.getText().toString(),
                                    response.getString("token")
                            )).get(UserInfoViewModel.class);




                } catch (JSONException e) {
                    Log.e("JSON2 Parse Error", e.getMessage());

                }
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
}
