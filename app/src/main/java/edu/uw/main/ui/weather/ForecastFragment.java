package edu.uw.main.ui.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.main.R;
import edu.uw.main.databinding.FragmentForecastBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends Fragment {
    private FragmentForecastBinding binding;
    public ForecastFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForecastBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        binding.buttonBack2.setOnClickListener(button ->
                processBack());

    }
    public void processBack(){
        Navigation.findNavController(getView()).navigate(
                ForecastFragmentDirections.actionForecastFragmentToWeatherActivity()
        );
    }
}
