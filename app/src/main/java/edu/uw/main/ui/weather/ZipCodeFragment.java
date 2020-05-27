package edu.uw.main.ui.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

import edu.uw.main.MainActivity;
import edu.uw.main.R;
import edu.uw.main.databinding.FragmentZipCodeBinding;
import edu.uw.main.model.UserInfoViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class ZipCodeFragment extends Fragment {

    private FragmentZipCodeBinding binding;
    private WeatherViewModel mWeatherModel;

    public ZipCodeFragment() {
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
        binding = FragmentZipCodeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity())
                .setActionBarTitle("Search Weather By Zip Code");
        UserInfoViewModel model = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

        mWeatherModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeWeatherResponse);

        mWeatherModel = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);
        binding.imageButton.setOnClickListener(button -> handleCurrent(model.getmJwt()));

    }

    /**
     * Warns user if zip code input is blank.
     * If not blank, sends zip code and jwt to WeatherViewModel
     * @param jwt Jason Web Token
     */
    public void handleCurrent(String jwt){
        if (binding.editText.getText().toString().length() > 0) {
            mWeatherModel.connectCurrent(jwt, binding.editText.getText().toString());
        } else {
            binding.editText.setError("Please enter a zip code");
        }
    }


    private void observeWeatherResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.currentWeather.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON1 Parse Error", e.getMessage());
                }
            } else if (response.has("coord")) {
                parseZip(response);
            } else if (response.has("cod")) {
                binding.editText.setError("City not found");
            }
        } else {
            Log.d("JSON Response", "No Response!");
        }
    }

    /**
     * Converts response into parsed data.
     * @param response The JSONObject bundle response.
     */
    private void parseZip(final JSONObject response){
        try {
            String weather = response.getJSONArray("weather").getJSONObject(0).get("description").toString();
            int temp = response.getJSONObject("main").getInt("temp");
            int feels_like =response.getJSONObject("main").getInt("feels_like");
            int pressure =response.getJSONObject("main").getInt("pressure");
            int humidity =response.getJSONObject("main").getInt("humidity");
            int sunrise = response.getJSONObject("sys").getInt("sunrise");
            int sunset = response.getJSONObject("sys").getInt("sunset");
            String city = response.getString("name");
            String whole = "Weather: " + weather + "\n"
                    + "Temp: " + kelvinToFahrenheit(temp) + "\u00B0 F\n"
                    + "Feels Like: " + kelvinToFahrenheit(feels_like) + "\u00B0 F\n"
                    + "Pressure: " + (pressure * 100) / 6895 + " PSI\n"
                    + "Humidity: " + humidity + "%\n"
                    + "Sunrise: " + getDate(sunrise) + " AM\n"
                    + "Sunset: " + getDate(sunset) + " PM\n"
                    + city;
            binding.currentWeather.setText(whole);
        }
        catch (JSONException e) {
            Log.e("JSON1 Parse Error", e.getMessage());
        }
    }

    /**
     * Convert Kelvins to Fahrenheit.
     * @param k Kelvins
     * @return Fahrenheit.
     */
    private double kelvinToFahrenheit(int k){
        double temp = ((k-273.15) * (9.0/5.0)) + 32;
        return Math.floor(temp * 100) / 100;
    }

    /**
     * Parse date and time.
     * @param time The time.
     * @return Parsed time.
     */
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
