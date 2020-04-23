package edu.uw.main.ui.auth;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

//import edu.uw.tcss450.LoginFragmentDirections;
import edu.uw.main.PasswordValidator;
import edu.uw.main.databinding.FragmentLoginBinding;
//import edu.uw.main.databinding.FragmentLoginBinding;

import static edu.uw.main.PasswordValidator.ValidationResult;
import static edu.uw.main.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.main.PasswordValidator.checkPwdLength;
import static edu.uw.main.PasswordValidator.checkPwdSpecialChar;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private PasswordValidator validate = checkPwdSpecialChar()
                                        .and(checkExcludeWhiteSpace())
                                        .and(checkPwdLength(0));
    private PasswordValidator pwdValidate = checkExcludeWhiteSpace()
                                            .and(checkPwdLength(0));

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        binding.buttonRegister.setOnClickListener(button ->
                processRegister());
        binding.buttonSuccess.setOnClickListener(button ->
                processSuccess());
    }
    public void processRegister(){

        Navigation.findNavController(getView()).navigate(
                LoginFragmentDirections.actionLoginFragmentToRegisterFragment2()
        );
    }

    public void processSuccess(){

        validate.processResult(validate
                    .apply(binding.textEmail.getText().toString())
                    .filter(result -> result != ValidationResult.SUCCESS),
                    this::processPassword,
                    this::handleEmailError);



    }

    private void handleEmailError(ValidationResult result) {
        String report = errorCode(result);
         binding.textEmail.setError(report);
    }
    private void processPassword(){
        pwdValidate.processResult(pwdValidate
                        .apply(binding.textPassword.getText().toString())
                        .filter(result -> result != ValidationResult.SUCCESS),
                this::success,
                this::handleSuccessError);
    }
    private void handleSuccessError(ValidationResult result) {
       String report = errorCode(result);
        binding.textPassword.setError(report);
    }
    private void success() {

        String message = binding.textPassword.getText().toString();
        Log.d("TESTER", String.valueOf(message.length()));
        Navigation.findNavController(getView()).navigate(
                LoginFragmentDirections.actionLoginFragmentToSuccessFragment(
                        binding.textEmail.getText().toString(), " "
                )
        );
    }
    private String errorCode(ValidationResult result){
        switch(result){
            case PWD_MISSING_SPECIAL :
                return "Invalid: Email missing @ symbol.";
            case PWD_INCLUDES_WHITESPACE:
            case PWD_INVALID_LENGTH:
                return "Invalid: Field is empty or contains whitespace in text.";

        }
        return null;
    }


}
