package edu.uw.main.ui.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

import edu.uw.main.MainActivity;
import edu.uw.main.R;

import edu.uw.main.databinding.FragmentLocationBinding;
import edu.uw.main.model.UserInfoViewModel;


public class LocationFragment extends Fragment implements GoogleMap.OnMapClickListener, OnMapReadyCallback {
    private FragmentLocationBinding binding;
    private WeatherViewModel mModel;
    private GoogleMap mMap;




    public LocationFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity())
                .get(WeatherViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) getActivity())
                .setActionBarTitle("Weather");

        mModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeWeatherResponse);

        FragmentLocationBinding binding = FragmentLocationBinding.bind(getView());

//        mWeatherModel.addResponseObserver(
//                getViewLifecycleOwner(),
//                this::observeWeatherResponse);

//        mModel = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);
//        mModel.addLocationObserver(getViewLifecycleOwner(),
//                location -> binding.textLatLong.setText(location.toString()));

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        WeatherViewModel model = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);
        model.addLocationObserver(getViewLifecycleOwner(), location -> {
            if (location != null) {
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.setMyLocationEnabled(true);

                final LatLng c = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(c, 15.0f));
            }
        });
        mMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.d("LAT/LONG", latLng.toString());
        UserInfoViewModel model = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

        mModel.connectLatLon(model.getmJwt(), latLng.latitude,latLng.longitude);
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title("New Marker"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, mMap.getCameraPosition().zoom));
    }

    private void observeWeatherResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.textLatLong.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON1 Parse Error", e.getMessage());
                }
            } else {

                parseHourly(response);
                Log.e("Button Pressed", "Current");

                //binding.textWeather.setText(response.toString());
                Log.e("Response", response.toString());
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
    private void parseHourly(final JSONObject response){
        Log.d("got here", "gotten");
        String date = "";
        float temp = 0;
        String descrip = "";
        String city = "";
        String state = "";
        String country = "";
        String whole = "";
        try {
            for (int i =  0; i < 1; i++) {
                JSONArray jsTemp = response.getJSONArray("data");

                // date = jsTemp.getJSONObject(i).get("timestamp_local").toString();

                temp = Float.parseFloat(jsTemp.getJSONObject(i).get("temp").toString());

                descrip = jsTemp.getJSONObject(i).getJSONObject("weather").get("description").toString();

                city = response.getString("city_name");

                state = response.getString("state_code");

                country = response.getString("country_code");

                String theString = "Current temperature: " + celsiusToFahrenheit(temp) + "°F\n"
                        + descrip + "\n" + city + ", " + state + " " + country + "\n";
                whole += theString;
            }

            FragmentLocationBinding binding = FragmentLocationBinding.bind(getView());
            binding.textLatLong.setText(whole);
        }
        catch (JSONException e) {
            Log.e("JSON1 Parse Error", e.getMessage());
        }
    }

    private int celsiusToFahrenheit(float c){
        int temp = (int) ((c/5) * 9 + 32);
        return temp;
    }
    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("hh:mm:ss", cal).toString();
        return date;
    }
}
