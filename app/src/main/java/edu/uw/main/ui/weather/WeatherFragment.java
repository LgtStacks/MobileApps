package edu.uw.main.ui.weather;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.main.MainActivity;
import edu.uw.main.R;
import edu.uw.main.databinding.FragmentWeatherBinding;
import edu.uw.main.model.UserInfoViewModel;


/**
 * The main fragment to handle the weather.
 * @author Group 3
 * @version 5/5
 */
public class WeatherFragment extends Fragment {
    private MutableLiveData<List<WeatherPost>> mWeatherList;

    private UserInfoViewModel mUserModel;


    private FragmentWeatherBinding binding;
    private WeatherViewModel mWeatherModel;
    //private GoogleMap mMap;


    /**
     * Default constructor for the weather fragment.
     */
    public WeatherFragment() {
        mWeatherList = new MutableLiveData<>();
        mWeatherList.setValue(new ArrayList<>());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeatherModel = new ViewModelProvider(getActivity())
                .get(WeatherViewModel.class);

        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        //mModel.connectGet(mUserModel.getmJwt());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /**
         binding = FragmentWeatherBinding.inflate(inflater, container, false);
         binding.layoutWait.setVisibility(View.INVISIBLE);
         return binding.getRoot();
         */
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){

        super.onViewCreated(view, savedInstanceState);
        mWeatherList = new MutableLiveData<>();
        mWeatherList.setValue(new ArrayList<>());
        binding = FragmentWeatherBinding.bind(getView());
        ((MainActivity) getActivity())
                .setActionBarTitle("Weather");


        mWeatherModel.addWeatherListViewModel(getViewLifecycleOwner(), weatherList -> {
            if (!weatherList.isEmpty()) {
                binding.listRoot.setAdapter(
                        new WeatherRecyclerViewAdapter(weatherList)
                );
                //binding.layoutWait.setVisibility(View.GONE);
            }
        });
        Calendar cc = Calendar.getInstance();
        int mHour = cc.get(Calendar.HOUR_OF_DAY);
        int mHour2 = cc.get(Calendar.HOUR);
        Log.e("Hour of Day", String.valueOf(mHour));
        Log.e("Hour", String.valueOf(mHour2));
        UserInfoViewModel model = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);


        //INSERT WEATHER RESPONSE
        mWeatherModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeWeatherResponse);


        // FragmentWeatherBinding binding = FragmentWeatherBinding.bind(getView());

        //   mWeatherModel = new ViewModelProvider(getActivity()).get(WeatherListViewModel.class);

        mWeatherModel.addLocationObserver(getViewLifecycleOwner(),
                location -> {
                    if (location != null) {
                        mWeatherModel.connectLatLon(model.getmJwt(), location.getLatitude(), location.getLongitude());
                        mWeatherModel.connectLatLonForecast(model.getmJwt(), location.getLatitude(), location.getLongitude());
                    }
                });

/**
 mWeatherModel.addLocationObserver(getViewLifecycleOwner(),
 location -> {
 if (location != null) {
 mWeatherModel.connectLatLonForecast(model.getmJwt(), location.getLatitude(), location.getLongitude());
 }
 });
 <<<<<<< Updated upstream
 binding.buttonZipCode.setOnClickListener(button ->
 Navigation.findNavController(getView()).
 navigate(WeatherFragmentDirections.actionWeatherActivityToZipCodeFragment()));
 binding.buttonLocation.setOnClickListener(button ->
 =======
 */
        Log.e("DATA INPUT: ", mWeatherList.getValue().toString());


    }

    private void observeWeatherResponse(final JSONObject response) {
        Log.e("Step", "1");
        // mWeatherList.setValue(new ArrayList<>());

        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    String error = "Error Authenticating: " +
                            response.getJSONObject("data").getString("message");
                    mWeatherList.getValue().add(new WeatherPost.Builder("Error", error).build());

                } catch (JSONException e) {
                    Log.e("JSON1 Parse Error", e.getMessage());
                }
            } else if (response.has("data")) {
                Log.e("Step", "2");

                parseForecast(response);

                //  Log.e("ForecastData", "Forecast");
                parseHourly(response);
                // Log.e("HourlyData", "Hourly");
                //  Log.e("TEST3", mWeatherList.getValue().toString());

            }
        } else {
            Log.d("JSON Response", "No Response");
        }
        Log.e("Step", "5");
        adjustList();


    }
    private void adjustList(){
        MutableLiveData<List<WeatherPost>> mUpdateList =  new MutableLiveData<>();
        mUpdateList.setValue(new ArrayList<>());
        Map<String, Integer> correctPosition = new HashMap<String, Integer>();
        correctPosition.put("Current", 0);
        correctPosition.put("Hourly", 1);
        correctPosition.put("Forecast", 2);

        ArrayList<String> checkDuplicate = new ArrayList<String>();
        //Remove Duplicate entries in the weatherlist.
        for(int i = 0; i < mWeatherList.getValue().size(); i++){

            String title = mWeatherList.getValue().get(i).getTitle();
            Log.e("CHECK DATA TITLE", title);
            if(checkDuplicate.contains(title)){
                mWeatherList.getValue().remove(i);
                i--;
            } else{
                mUpdateList.getValue().add(mWeatherList.getValue().get(i));
                checkDuplicate.add(title);
            }
        }
        boolean check = false;
        if(mUpdateList.getValue().size() == 3){
            check = true;
            for(int i = 0; i < 3; i++){

                String title = mUpdateList.getValue().get(i).getTitle();
                int index = correctPosition.get(title);
                mWeatherList.getValue().set(index, mUpdateList.getValue().get(i));
            }
        }
        if(check){
            mWeatherModel.setMutableData(mWeatherList);
        }
    }
    private void parseForecast(final JSONObject response){

        Log.d("Forecast!", "gotten");
        String date = "";
        float highTemp = 0;
        float lowTemp = 0;
        String descrip = "";
        String whole = "";
        Log.e("Step", "3.1");

        try {
            for (int i = 0; i < 9; i++) {
                Log.e("Check Loop", String.valueOf(i));
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
            Log.e("Step", "3.2");
            mWeatherList.getValue().add(new WeatherPost.Builder("Forecast", whole).build());
            //Log.e("TEST2", mWeatherList.getValue().toString());

            // binding.textWeatherForecast.setText(whole);
        } catch (JSONException e) {
            Log.e("JSON1 Parse Error", e.getMessage());
        }

    }
    private void parseHourly(final JSONObject response){
        Log.d("hourly!", "gotten");
        String date = "";
        float temp = 0;
        float highTemp = 0;
        float lowTemp = 0;
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
            Log.e("Step", "4");
            mWeatherList.getValue().add(new WeatherPost.Builder("Current", part).build());
            //Log.e("CURRENT DATA", mWeatherList.getValue().get(1).getTitle()) ;

            //binding.textWeatherCurrent.setText(part);

            for (int i =  1; i < 24; i++) {
                JSONArray jsTemp = response.getJSONArray("data");

                date = jsTemp.getJSONObject(i).get("timestamp_local").toString();

                temp = Float.parseFloat(jsTemp.getJSONObject(i).get("temp").toString());

                descrip = jsTemp.getJSONObject(i).getJSONObject("weather").get("description").toString();

                String theString = date + " - "
                        + "Temp: " + temp + " - "
                        + "Weather: " +  " " + descrip + "\n";
                whole += theString ;
            }
            mWeatherList.getValue().add(new WeatherPost.Builder("Hourly", whole).build());

            //   Log.e("TESTE", mWeatherList.getValue().toString());
            //binding.textWeatherHourly.setText(whole);
        }
        catch (JSONException e) {
            Log.e("JSON1 Parse Error", e.getMessage());
        }
    }
    private int celsiusToFahrenheit(float c){
        int temp = (int) ((c/5) * 9 + 32);
        return temp;
    }
//    @Override
//    public void onResume() {
//        if (MainActivity.changePassword) {
//            MainActivity.changePassword = false;
//            Navigation.findNavController(getView()).navigate(WeatherFragmentDirections.actionWeatherActivityToChange2());
//        }
//        super.onResume();
//    }
}
