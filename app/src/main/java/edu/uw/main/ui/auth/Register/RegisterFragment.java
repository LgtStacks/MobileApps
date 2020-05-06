package edu.uw.main.ui.auth.Register;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.main.PasswordValidator;
import edu.uw.main.databinding.FragmentRegisterBinding;
import edu.uw.main.ui.auth.Register.RegisterFragmentDirections;

import static edu.uw.main.PasswordValidator.checkClientPredicate;
import static edu.uw.main.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.main.PasswordValidator.checkPwdDigit;
import static edu.uw.main.PasswordValidator.checkPwdLength;
import static edu.uw.main.PasswordValidator.checkPwdLowerCase;
import static edu.uw.main.PasswordValidator.checkPwdSpecialChar;
import static edu.uw.main.PasswordValidator.checkPwdUpperCase;


/**
 * A fragment to handle the register page.
 * @author Group 3
 * @version 5/5
 */
public class RegisterFragment extends Fragment {
    /** Our binding for this fragment. */
    private FragmentRegisterBinding binding;


    private RegisterViewModel mRegisterModel;

    /** Name validator for our name fields. Requirements: Name length > 1 */
    private PasswordValidator mNameValidator = checkPwdLength(1);

    /** Email validator for our email field. Requirements: Length > 2, No white space, contains a @ sign. */
    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    /** Password validator for our pw field. Requirements: Pw's are the same, length > 6, special char, no white space, contains digit and letter. */
    private PasswordValidator mPassWordValidator =
            checkClientPredicate(pwd -> pwd.equals(binding.textRepaswd.getText().toString()))
                    .and(checkPwdLength(7))
                    .and(checkPwdSpecialChar())
                    .and(checkExcludeWhiteSpace())
                    .and(checkPwdDigit())
                    .and(checkPwdLowerCase().or(checkPwdUpperCase()));

    /**'
     * Default constructor
     */
    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegisterModel = new ViewModelProvider(getActivity())
                .get(RegisterViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSuccess.setOnClickListener(this::attemptRegister);
        mRegisterModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);
    }

    /**
     * Attempts to register the user. Goes through all the checks.
     * @param button - Method reference variable.
     */
    private void attemptRegister(final View button) {
        validateFirst();
    }

    /**
     * Validates first name field and then calls last name validation method.
     */
    private void validateFirst() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.textFirstName.getText().toString().trim()),
                this::validateLast,
                result -> binding.textEmail.setError("Please enter a valid Email address."));
    }

    /**
     * Validates last name field and calls for email validation.
     */
    private void validateLast() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.textLastName.getText().toString().trim()),
                this::validateEmail,
                result -> binding.textEmail.setError("Please enter a valid Email address."));
    }

    /**
     * Validates email and calls for password validation.
     */
    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.textEmail.getText().toString().trim()),
                this::validatePassword,
                result -> binding.textEmail.setError("Please enter a valid Email address."));
    }

    /**
     * Validates password and calls for server authentication validation.
     */
    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.textPassword.getText().toString()),
                this::verifyAuthWithServer,
                result -> binding.textPassword.setError("Please enter a valid Password."));
    }

    /**
     * Sends in Full name, email, and password to the server.
     */
    private void verifyAuthWithServer() {
        mRegisterModel.connect(
                binding.textFirstName.getText().toString(),
                binding.textLastName.getText().toString(),
                binding.textEmail.getText().toString(),
                binding.textPassword.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect()
    }

    /**
     * Naviagtes to the verify fragment page.
     */
    private void navigateToLogin() {
        Navigation.findNavController(getView()).navigate(RegisterFragmentDirections.actionRegisterFragmentToVerificationFragment());
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to SignInViewModel.
     *
     * @param response the Response from the server
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.textEmail.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                navigateToLogin();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }

    }
}
