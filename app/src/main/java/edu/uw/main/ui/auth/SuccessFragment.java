package edu.uw.main.ui.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.main.databinding.FragmentSuccessBinding;
import edu.uw.main.model.UserInfoViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class SuccessFragment extends Fragment {

    private FragmentSuccessBinding binding;

    public SuccessFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSuccessBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserInfoViewModel model = new ViewModelProvider(getActivity())

                .get(UserInfoViewModel.class);

      //  SuccessFragmentArgs args = SuccessFragmentArgs.fromBundle(getArguments());

        binding.textMessage.setText(model.getEmail());

    }
}
