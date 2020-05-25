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
    private GoogleMap mMap;

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
//        binding.buttonCurrent.setOnClickListener(button ->
//                handleCurrent(model.getmJwt()));
//        binding.buttonForecast.setOnClickListener(button ->
//                handleForecast(model.getmJwt()));
//        binding.buttonHourly.setOnClickListener(button ->
//                handleHourly(model.getmJwt()));
        mWeatherModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeWeatherResponse);

        FragmentWeatherBinding binding = FragmentWeatherBinding.bind(getView());

        mWeatherModel = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);
        mWeatherModel.addLocationObserver(getViewLifecycleOwner(),
                location -> mWeatherModel.connectCurrent(model.getmJwt()));
        mWeatherModel.addLocationObserver(getViewLifecycleOwner(),
                location -> mWeatherModel.connectHourly(model.getmJwt()));
        mWeatherModel.addLocationObserver(getViewLifecycleOwner(),
                location -> mWeatherModel.connectForecast(model.getmJwt()));

        binding.button.setOnClickListener(button ->
                Navigation.findNavController(getView()).
                        navigate(WeatherFragmentDirections.actionWeatherActivityToLocationFragment()));

    }

//    public void handleCurrent(String jwt){
//
//      //  mWeatherModel.connectCurrent(jwt);
//
//        binding.layoutWait.setVisibility(View.VISIBLE);
//    }
//    public void handleForecast(String jwt){
//
//        mWeatherModel.connectForecast(jwt);
//
//        binding.layoutWait.setVisibility(View.VISIBLE);
//    }
//    public void handleHourly(String jwt){
//
//        mWeatherModel.connectHourly(jwt);
//
//        binding.layoutWait.setVisibility(View.VISIBLE);
//    }

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
            } else {
                if(response.has("list")){
                    parseForecast(response);
                    Log.e("Button Pressed", "Forecast");
                }
                else if(response.has("data")){
                    parseHourly(response);
                    Log.e("Button Pressed", "Hourly");
                }
                else {
                    parseCurrent(response);
                    Log.e("Button Pressed", "Current");
                }
                //binding.textWeather.setText(response.toString());
                Log.e("Response", response.toString());
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
    private void parseForecast(final JSONObject response){
        try {
            int counter = 1;
            int maxTemp = 0;
            int minTemp = 10000000;
            String whole = "";
            String date = "";
            String weather = "";
            JSONArray jsTemp = response.getJSONArray("list");
            for(int i = 0; i < 40; i++){
                JSONObject currObj = jsTemp.getJSONObject(i);
                if(counter > 7){
                    String theString = date + ": High: " + kelvinToFahrenheit(maxTemp) + "\u00B0 F"
                            + " Low: " + kelvinToFahrenheit(minTemp) + "\u00B0 F"
                            + " Weather: " + weather + "\n";
                    counter = 0;
                    maxTemp = 0;
                    minTemp = 100000000;
                    whole += theString;
                }
                if(counter == 1) {
                    weather = currObj.getJSONArray("weather")
                            .getJSONObject(0)
                            .get("description")
                            .toString();
                    date = currObj.get("dt_txt").toString().split(" ")[0];
                }
                int currentMax = currObj.getJSONObject("main").getInt("temp_max");
                Log.e("MAX", String.valueOf(currentMax));
                int currentMin =currObj.getJSONObject("main").getInt("temp_min");
                Log.e("MIN", String.valueOf(currentMin));
                if(currentMax > maxTemp) {
                    maxTemp = currentMax;
                }
                if(currentMin < minTemp) {
                    minTemp = currentMin;
                }
                counter++;
            }
            binding.textWeatherForecast.setText(whole);
        } catch (JSONException e) {
            Log.e("JSON1 Parse Error", e.getMessage());
        }

    }
    private void parseHourly(final JSONObject response){
        Log.d("got here", "gotten");
        String date = "";
        float temp = 0;
        String descrip = "";
        String whole = "";
        try {
            for (int i =  0; i < 24; i++) {
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
    private void parseCurrent(final JSONObject response){
        try {
            String weather = response.getJSONArray("weather").getJSONObject(0).get("description").toString();
            int temp = response.getJSONObject("main").getInt("temp");
            int feels_like =response.getJSONObject("main").getInt("feels_like");
            int pressure =response.getJSONObject("main").getInt("pressure");
            int humidity =response.getJSONObject("main").getInt("humidity");
            int sunrise = response.getJSONObject("sys").getInt("sunrise");
            int sunset = response.getJSONObject("sys").getInt("sunset");
            String whole = "Weather: " + weather + "\n"
                    + "Temp: " + kelvinToFahrenheit(temp) + "\u00B0 F\n"
                    + "Feels Like: " + kelvinToFahrenheit(feels_like) + "\u00B0 F\n"
                    + "Pressure: " + (pressure * 100) / 6895 + " PSI\n"
                    + "Humidity: " + humidity + "%\n"
                    + "Sunrise: " + getDate(sunrise) + " AM\n"
                    + "Sunset: " + getDate(sunset) + " PM\n";
            binding.textWeatherCurrent.setText(whole);
        }
        catch (JSONException e) {
            Log.e("JSON1 Parse Error", e.getMessage());
        }
    }

    private double kelvinToFahrenheit(int k){
        double temp = (k-273.15)* (9/5) + 32;
        return Math.floor(temp * 100) / 100;
    }
    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("hh:mm:ss", cal).toString();
        return date;
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
