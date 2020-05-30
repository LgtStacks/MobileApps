package edu.uw.main.ui.weather;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.main.MainActivity;
import edu.uw.main.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherMain extends Fragment {

    public WeatherMain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = getActivity().findViewById(R.id.tab_layout);
        view.setVisibility(View.VISIBLE);
        View view2 = getActivity().findViewById(R.id.frameLayout5);
        view2.setVisibility(View.VISIBLE);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_main, container, false);
    }
}
