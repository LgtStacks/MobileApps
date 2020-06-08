package edu.uw.main.ui.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.Navigation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uw.main.MainActivity;
import edu.uw.main.R;
import edu.uw.main.databinding.FragmentSuccessBinding;
import edu.uw.main.databinding.FragmentWeatherBinding;
import edu.uw.main.model.UserInfoViewModel;
import edu.uw.main.ui.weather.WeatherPost;
import edu.uw.main.ui.weather.WeatherRecyclerViewAdapter;
import edu.uw.main.ui.weather.WeatherViewModel;


/**
 * A framgent to handle the home page.
 * The user has successfully logged into the app.
 * @author Group 3
 * @version 5/5
 */
public class SuccessFragment extends Fragment {

    private FragmentSuccessBinding binding;
    private WeatherViewModel mWeatherModel;

    /**
     * Default constructor.
     * @author Group 3
     * @version 5/5
     */
    public SuccessFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mWeatherModel = new ViewModelProvider(getActivity())
                .get(WeatherViewModel.class);
        View view = getActivity().findViewById(R.id.tab_layout);
        view.setVisibility(View.GONE);
        View view2 = getActivity().findViewById(R.id.frameLayout5);
        View view3 = getActivity().findViewById(R.id.zipCodeLayout);
        View view4 = getActivity().findViewById(R.id.layout_root);

        if (view2 != null) {
            view2.setVisibility(View.GONE);
        }
        if (view3 != null) {
            view3.setVisibility(View.GONE);
        }
        if (view4 != null) {
            view4.setVisibility(View.GONE);
        }

        ((MainActivity) getActivity())
                .setActionBarTitle("Guild Banner Home");

        binding = FragmentSuccessBinding.inflate(inflater, container, false);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserInfoViewModel model = new ViewModelProvider(getActivity())

                .get(UserInfoViewModel.class);

        //  SuccessFragmentArgs args = SuccessFragmentArgs.fromBundle(getArguments());
        binding.textMessage.setText("Hello " + model.getEmail());

        //INSERT WEATHER RESPONSE
        mWeatherModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeWeatherResponse);
    }

    /**
     * Observes a new weather response update.
     * @param response the response.
     */
    private void observeWeatherResponse(final JSONObject response) {
        Log.e("Step", "1");
        // mWeatherList.setValue(new ArrayList<>());

        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    String error = "Error Authenticating: " +
                            response.getJSONObject("data").getString("message");
                  //  mWeatherList.getValue().add(new WeatherPost.Builder("Error", error).build());

                } catch (JSONException e) {
                    Log.e("JSON1 Parse Error", e.getMessage());
                }
            } else if (response.has("data")) {
                Log.e("Step", "2");
                parseHourly(response);
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
        Log.e("Step", "5");
    }


    /**
     * Private helper method to parse the hourly.
     * @param response the response.
     */
    private void parseHourly(final JSONObject response){
        Log.d("hourly!", "gotten");
        String date = "";
        float temp = 0;
        float highTemp = 0;
        float lowTemp = 0;
        String code = "";
        String descrip = "";
        String city = "";
        String state = "";
        String country = "";
        String part = "";
        String whole = "";
        try {
            for (int i =  0; i < 1; i++) {
                JSONArray jsTemp = response.getJSONArray("data");

                code = jsTemp.getJSONObject(i).getJSONObject("weather").get("code").toString();

                temp = Float.parseFloat(jsTemp.getJSONObject(i).get("temp").toString());

                descrip = jsTemp.getJSONObject(i).getJSONObject("weather").get("description").toString();

                city = response.getString("city_name");

                state = response.getString("state_code");

                country = response.getString("country_code");

                String theString = "Current temperature: \n" + celsiusToFahrenheit(temp) + "°F\n"
                        + descrip + "\n" + city + ", " + state + " " + country + "\n";
                part += theString;
            }
            Log.e("Step", "4");
            binding.textViewW.setText(part);

            String timeStamp = new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime());
            boolean dayTime = Integer.parseInt(timeStamp) > 60000 && Integer.parseInt(timeStamp) < 190000;
            getIcon(code, dayTime);
        }
        catch (JSONException e) {
            Log.e("JSON1 Parse Error", e.getMessage());
        }
    }

    /**
     *  converts celcius to fahrenheit
     * @param c the temp in celcius
     * @return the temp in fahrenheit
     */
    private int celsiusToFahrenheit(float c){
        int temp = (int) ((c/5) * 9 + 32);
        return temp;
    }

    /**
     * Gets the icon representing the weather.
     * @param code ID of weather conditions
     * @param dayTime T/F day or night
     */
    private void getIcon(String code, boolean dayTime) {
        if (code.equals("200") || code.equals("201") || code.equals("202")
                || code.equals("230") || code.equals("231") || code.equals("232")
                || code.equals("233"))  {
            binding.imageView6.setImageResource(R.drawable.daynightthunderstorm);
        } else if (code.equals("300") || code.equals("301") || code.equals("500")
                || code.equals("501") || code.equals("511") || code.equals("520")
                || code.equals("521")) {
            if (dayTime) {
                binding.imageView6.setImageResource(R.drawable.dayrain);
            } else {
                binding.imageView6.setImageResource(R.drawable.nightrain);
            }
        } else if (code.equals("302") || code.equals("502") || code.equals("522")) {
            binding.imageView6.setImageResource(R.drawable.daynightshowerrain);
        } else if (code.equals("600") || code.equals("601") || code.equals("602")
                || code.equals("610") || code.equals("611") || code.equals("612")
                || code.equals("621") || code.equals("622") || code.equals("623")) {
            binding.imageView6.setImageResource(R.drawable.daynightsnow);
        } else if (code.equals("700") || code.equals("711") || code.equals("721")
                || code.equals("731") || code.equals("741") || code.equals("751")) {
            binding.imageView6.setImageResource(R.drawable.daynightmist);
        } else if (code.equals("800")) {
            if (dayTime) {
                binding.imageView6.setImageResource(R.drawable.dayclearsky);
            } else {
                binding.imageView6.setImageResource(R.drawable.nightclearsky);
            }
        } else if (code.equals("801")) {
            if (dayTime) {
                binding.imageView6.setImageResource(R.drawable.dayfewclouds);
            } else {
                binding.imageView6.setImageResource(R.drawable.nightfewclouds);
            }
        } else if (code.equals("802")) {
            binding.imageView6.setImageResource(R.drawable.daynightscatteredclouds);
        } else if (code.equals("803") || code.equals("804")) {
            binding.imageView6.setImageResource(R.drawable.daynightbrokenclouds);
        } else if (code.equals("900")) {
            if (dayTime) {
                binding.imageView6.setImageResource(R.drawable.dayrain);
            } else {
                binding.imageView6.setImageResource(R.drawable.nightrain);
            }
        }
    }

    @Override
    public void onResume() {
        if (MainActivity.changePassword) {
            MainActivity.changePassword = false;
            Navigation.findNavController(getView()).navigate(SuccessFragmentDirections.actionNavigationHomeToChange2());
        }
        super.onResume();

    }
}
