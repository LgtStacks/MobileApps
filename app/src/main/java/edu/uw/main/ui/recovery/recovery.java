package edu.uw.main.ui.recovery;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import edu.uw.main.R;
import edu.uw.main.databinding.FragmentRecoveryBinding;
import edu.uw.main.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class recovery extends Fragment {

    /** Our binding for this fragment. */
    private FragmentRecoveryBinding binding;

    private RecoveryViewModel mRecoveryModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRecoveryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mRecoveryModel = new ViewModelProvider(getActivity())
                .get(RecoveryViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSend.setOnClickListener(button -> navigateToLogin());
    }

    private void navigateToLogin() {
        mRecoveryModel.connect(binding.textEmailRecover.getText().toString());
        Navigation.findNavController(getView()).navigate(recoveryDirections.actionRecoveryToLoginFragment());
    }
}
