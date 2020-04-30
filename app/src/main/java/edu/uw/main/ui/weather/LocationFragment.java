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
import edu.uw.main.databinding.FragmentLocationBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class LocationFragment extends Fragment {
    private FragmentLocationBinding binding;

    public LocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLocationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        binding.buttonBack.setOnClickListener(button ->
                processBack());

    }
    public void processBack(){
        Navigation.findNavController(getView()).navigate(
            LocationFragmentDirections.actionLocationFragmentToWeatherActivity()
        );
    }

}
