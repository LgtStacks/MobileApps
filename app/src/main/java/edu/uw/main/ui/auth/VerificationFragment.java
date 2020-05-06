package edu.uw.main.ui.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.Navigation;
import edu.uw.main.databinding.FragmentVerificationBinding;

/**
 * A verification page to ensure that tells the user to check their email.
 * @author Group 3
 * @version 5/5
 */
public class VerificationFragment extends Fragment {

    private FragmentVerificationBinding binding;

    /**
     * Default constructor.
     */
    public VerificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVerificationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        binding.buttonContinue.setOnClickListener(button -> navigateToLogin());

    }

    /**
     * Navigate to the login page after verification notification is given.
     */
    private void navigateToLogin() {
        Navigation.findNavController(getView()).navigate(VerificationFragmentDirections.actionVerificationFragmentToLoginFragment());
    }

}
