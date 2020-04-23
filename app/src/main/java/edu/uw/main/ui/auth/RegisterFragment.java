package edu.uw.main.ui.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.main.PasswordValidator;
import edu.uw.main.databinding.FragmentRegisterBinding;

import static edu.uw.main.PasswordValidator.checkClientPredicate;
import static edu.uw.main.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.main.PasswordValidator.checkPwdLength;
import static edu.uw.main.PasswordValidator.checkPwdNull;
import static edu.uw.main.PasswordValidator.checkPwdSpecialChar;
import static edu.uw.main.PasswordValidator.checkPwdUpperCase;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private PasswordValidator validate = checkPwdSpecialChar()
                                         .and(checkExcludeWhiteSpace())
                                         .and(checkPwdNull());
    private PasswordValidator pwdValidate = checkExcludeWhiteSpace()
                                            .and(checkPwdNull())
                                            .and(checkPwdLength(5))
                                            .and(checkPwdUpperCase())
                                            .and(checkClientPredicate
                                                    (pwd -> pwd.equals(binding.textRepaswd.getText().toString())));


    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonSuccess.setOnClickListener(button ->
                processEmail(binding.textEmail.getText().toString()));

    }

    public void processEmail(String message){
        validate.processResult(validate
                        .apply(binding.textEmail.getText().toString())
                        .filter(result -> result != PasswordValidator.ValidationResult.SUCCESS),
                this::processSuccess,
                this::handleEmailError);

    }
    private void handleEmailError(PasswordValidator.ValidationResult result) {
        String report = errorCode(result);
        binding.textEmail.setError(report);
    }
    private void processSuccess(){
        pwdValidate.processResult(pwdValidate
                        .apply(binding.textPassword.getText().toString())
                        .filter(result -> result != PasswordValidator.ValidationResult.SUCCESS),
                this::success,
                this::handleSuccessError);
    }
    private void handleSuccessError(PasswordValidator.ValidationResult result) {
        String report = errorCode(result);
        binding.textPassword.setError(report);
    }
    private void success() {

        String message = binding.textEmail.getText().toString();
        Navigation.findNavController(getView()).navigate(
                RegisterFragmentDirections.actionRegisterFragmentToSuccessFragment(message, " ")
        );
    }
    private String errorCode(PasswordValidator.ValidationResult result){
        switch(result){
            case PWD_MISSING_SPECIAL :
                return "Invalid: Email missing @ symbol.";
            case PWD_INCLUDES_WHITESPACE:
            case PWD_MISSING_FIELD:
                return "Invalid: Field is empty or contains whitespace in text.";
            case PWD_INVALID_LENGTH:
                return "Invalid: Password is less than 6 characters.";
            case PWD_MISSING_UPPER:
                return "Invalid: Password does not contain upper case letter.";
            case PWD_CLIENT_ERROR:
                return "Invalid: Passwords does not match each other.";
        }
        return "ERROR: NO VALID REASON FOUND!";
    }

}
