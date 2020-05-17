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
import edu.uw.main.databinding.FragmentChangeBinding;
import edu.uw.main.model.UserInfoViewModel;
import edu.uw.main.ui.weather.WeatherViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class change extends Fragment {

    private FragmentChangeBinding  binding;

    private ChangePasswordViewModel mModel;

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
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        UserInfoViewModel model = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        binding.buttonChange.setOnClickListener(button -> navigateBack(model));
    }

   private void navigateBack(UserInfoViewModel model) {
       mModel.connect(binding.textNewPw.getText().toString(), model.getmJwt());
       getActivity().onBackPressed();
   }
}
