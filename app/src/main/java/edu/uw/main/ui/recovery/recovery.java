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
        mRecoveryModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeRecoveryResponse);
    }

    private void navigateToLogin() {
        Navigation.findNavController(getView()).navigate(recoveryDirections.actionRecoveryToLoginFragment());
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to SignInViewModel.
     *
     * @param response the Response from the server
     */
    private void observeRecoveryResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.textEmailRecover.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON1 Parse Error", e.getMessage());
                }
            } else {
                //put what we should do when this happens
            }
        } else {
            Log.d("JSON Response", "No Response");
        }

    }
}
