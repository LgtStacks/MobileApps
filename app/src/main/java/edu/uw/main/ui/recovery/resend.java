package edu.uw.main.ui.recovery;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import edu.uw.main.R;
import edu.uw.main.databinding.FragmentReVerificationBinding;
import edu.uw.main.databinding.FragmentVerificationBinding;
import edu.uw.main.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class resend extends Fragment {

    private FragmentReVerificationBinding binding;

    private ResendViewModel mResendModel;

    public resend() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReVerificationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Resend Verification");
        mResendModel = new ViewModelProvider(getActivity())
                .get(ResendViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        //UserInfoViewModel model = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        binding.btnResendSend.setOnClickListener(button -> navigateToResendVerification());
    }

    /**
     * This method sends the Email to the endpoint which then verifies if the user needs another verification email.
     */
    private void navigateToResendVerification() {
        mResendModel.connect(binding.textResendEmail.getText().toString());
        Navigation.findNavController(getView()).navigate(resendDirections.actionReVerificationToLoginFragment());
    }
}
