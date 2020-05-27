package edu.uw.main.ui.weather;

import android.app.ActionBar;
import android.os.Bundle;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.main.MainActivity;
import edu.uw.main.R;
import edu.uw.main.databinding.FragmentWeatherBinding;
import edu.uw.main.model.PushyTokenViewModel;
import edu.uw.main.model.UserInfoViewModel;
import edu.uw.main.ui.auth.Login.LoginViewModel;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



/**
 * The main fragment to handle the weather.
 * @author Group 3
 * @version 5/5
 */
public class WeatherFragment extends Fragment {
    private FragmentWeatherBinding binding;
    private WeatherViewModel mWeatherModel;
    //private GoogleMap mMap;

    /**
     * Default constructor for the weather fragment.
     */
    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mWeatherModel = new ViewModelProvider(getActivity())
                .get(WeatherViewModel.class);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        binding.layoutWait.setVisibility(View.INVISIBLE);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) getActivity())
                .setActionBarTitle("Weather");
        mWeatherModel.addResponseObserver(getViewLifecycleOwner(), response ->
                binding.layoutWait.setVisibility(View.GONE));
        Calendar cc = Calendar.getInstance();
        int mHour = cc.get(Calendar.HOUR_OF_DAY);
        int mHour2 = cc.get(Calendar.HOUR);
        Log.e("Hour of Day", String.valueOf(mHour));
        Log.e("Hour", String.valueOf(mHour2));
        UserInfoViewModel model = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

        mWeatherModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeWeatherResponse);


        FragmentWeatherBinding binding = FragmentWeatherBinding.bind(getView());

        mWeatherModel = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);

        mWeatherModel.addLocationObserver(getViewLifecycleOwner(),
                location -> {
                    if (location != null) {
                        mWeatherModel.connectLatLon(model.getmJwt(), location.getLatitude(), location.getLongitude());
                    }
                });

        mWeatherModel.addLocationObserver(getViewLifecycleOwner(),
                location -> {
                    if (location != null) {
                        mWeatherModel.connectLatLonForecast(model.getmJwt(), location.getLatitude(), location.getLongitude());
                    }
                });

        binding.buttonZipCode.setOnClickListener(button ->
                Navigation.findNavController(getView()).
                        navigate(WeatherFragmentDirections.actionWeatherActivityToZipCodeFragment()));

        binding.buttonLocation.setOnClickListener(button ->
                Navigation.findNavController(getView()).
                        navigate(WeatherFragmentDirections.actionWeatherActivityToLocationFragment()));

    }

    private void observeWeatherResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.textWeatherCurrent.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON1 Parse Error", e.getMessage());
                }
            } else if (response.has("data")) {
                parseForecast(response);
                Log.e("ForecastData", "Forecast");
                parseHourly(response);
                Log.e("HourlyData", "Hourly");
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
    private void parseForecast(final JSONObject response){
        Log.d("Forecast!", "gotten");
        String date = "";
        float highTemp = 0;
        float lowTemp = 0;
        String descrip = "";
        String whole = "";
        try {
            for (int i = 0; i < 9; i++) {
                JSONArray jsTemp = response.getJSONArray("data");

                date = jsTemp.getJSONObject(i).get("valid_date").toString();

                highTemp = Float.parseFloat(jsTemp.getJSONObject(i).get("high_temp").toString());

                lowTemp = Float.parseFloat(jsTemp.getJSONObject(i).get("app_min_temp").toString());

               descrip = jsTemp.getJSONObject(i).getJSONObject("weather").get("description").toString();

                String theString = date
                        + " High " + celsiusToFahrenheit(highTemp) + "°F "
                        + " Low " + celsiusToFahrenheit(lowTemp) + "°F "
                        + descrip + "\n";
                whole += theString;
            }
            binding.textWeatherForecast.setText(whole);
        } catch (JSONException e) {
            Log.e("JSON1 Parse Error", e.getMessage());
        }

    }
    private void parseHourly(final JSONObject response){
        Log.d("hourly!", "gotten");
        String date = "";
        float temp = 0;
        String descrip = "";
        String city = "";
        String state = "";
        String country = "";
        String part = "";
        String whole = "";
        try {
            for (int i =  0; i < 1; i++) {
                JSONArray jsTemp = response.getJSONArray("data");

                temp = Float.parseFloat(jsTemp.getJSONObject(i).get("temp").toString());

                descrip = jsTemp.getJSONObject(i).getJSONObject("weather").get("description").toString();

                city = response.getString("city_name");

                state = response.getString("state_code");

                country = response.getString("country_code");

                String theString = "Current temperature: " + celsiusToFahrenheit(temp) + "°F\n"
                        + descrip + "\n" + city + ", " + state + " " + country + "\n";
                part += theString;
            }
            binding.textWeatherCurrent.setText(part);

            for (int i =  1; i < 24; i++) {
                JSONArray jsTemp = response.getJSONArray("data");

                date = jsTemp.getJSONObject(i).get("timestamp_local").toString();

                temp = Float.parseFloat(jsTemp.getJSONObject(i).get("temp").toString());

                descrip = jsTemp.getJSONObject(i).getJSONObject("weather").get("description").toString();

                String theString = date + " - "
                        + "Temp: " + temp + " - "
                        + "Weather: " +  " " + descrip + "\n";
                whole += theString;
            }
            binding.textWeatherHourly.setText(whole);
        }
        catch (JSONException e) {
            Log.e("JSON1 Parse Error", e.getMessage());
        }
    }
    private int celsiusToFahrenheit(float c){
        int temp = (int) ((c/5) * 9 + 32);
        return temp;
    }
    @Override
    public void onResume() {
        if (MainActivity.changePassword) {
            MainActivity.changePassword = false;
            Navigation.findNavController(getView()).navigate(WeatherFragmentDirections.actionWeatherActivityToChange2());
        }
        super.onResume();
    }
}
