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
import edu.uw.main.databinding.FragmentWeatherBinding;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class WeatherFragment extends Fragment {
    private FragmentWeatherBinding binding;

    public WeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        binding.buttonForecast.setOnClickListener(button ->
                processForecast());
        binding.buttonLocation.setOnClickListener(button ->
                processLocation());
    }
    public void processForecast(){
        Navigation.findNavController(getView()).navigate(
                WeatherFragmentDirections.actionWeatherFragment2ToForecastFragment()
        );
    }
    public void processLocation(){
        Navigation.findNavController(getView()).navigate(
                WeatherFragmentDirections.actionWeatherActivityToLocationFragment()
        );
    }
}
