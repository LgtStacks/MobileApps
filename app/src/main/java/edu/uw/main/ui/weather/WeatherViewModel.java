package edu.uw.main.ui.weather;

import android.app.Application;
import android.location.Location;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.uw.main.io.RequestQueueSingleton;
/**
 * The class to handle the Weather View Model.
 * @author Group 3
 * @version 6/2
 */
public class WeatherViewModel extends AndroidViewModel {

   private MutableLiveData<List<WeatherPost>> mWeatherList;

    private MutableLiveData<JSONObject> mResponse;
    private MutableLiveData<Location> mLocation;

    /**
     * Standard constructor with parameters.
     * @param application new application for live data.
     */
    public WeatherViewModel(@NonNull Application application) {
        super(application);
        mWeatherList = new MutableLiveData<>();
        mWeatherList.setValue(new ArrayList<>());

        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
        mLocation = new MediatorLiveData<>();
    }
    /**
     * This method will add a response observer for interacting with the server.
     * @param owner Owner of the data.
     * @param observer JSON Object to report the result.
*/
    public void addWeatherListViewModel(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super List<WeatherPost>> observer) {
        mWeatherList.observe(owner, observer);
    }

    /**
     * This method will add a response observer for interacting with the server.
     * @param owner Owner of the data.
     * @param observer JSON Object to report the result.
     */
    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    /**
     * Adds a new location Observer.
     * @param owner the owner
     * @param observer the observer
     */
    public void addLocationObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super Location> observer) {
        mLocation.observe(owner, observer);
    }

    /**
     * Sets a new location
     * @param location
     */
    public void setLocation(final Location location) {
        if (!location.equals(mLocation.getValue())) {
            mLocation.setValue(location);
        }
    }

    /**
     * Sets the new mutable data.
     * @param mw updated mutable data.
     */
    public void setMutableData(final MutableLiveData<List<WeatherPost>> mw) {
        // Log.e("DATA OUTPUT: ", mw.getValue());
        // if(mWeatherList.getValue().hashCode() == mw.getValue().)
        mWeatherList.setValue(mw.getValue());
    }

    /**
     * gets the current weather data.
     * @param jwt
     * @param zip zip code.
     */
    public void connectCurrent(String jwt, String zip) {
        String url =
                "https://app-backend-server.herokuapp.com/weather/current/?q=" + zip + ", US";
        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null, //no body for this get request
                mResponse::setValue,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
        Log.d("observe!", "0");
    }

    /**
     * Forecast data connection.
     * @param jwt
     */
    public void connectForecast(String jwt) {
        String url =
                "https://app-backend-server.herokuapp.com/weather/forecast/?q=98402";
        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null, //no body for this get request
                mResponse::setValue,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }
//    public void connectHourly(String jwt) {
//        String url =
//                "https://app-backend-server.herokuapp.com/weather/hourly/?postal_code=98502&country=US";
//               // "https://app-backend-server.herokuapp.com/weather/hourly";
//        Request request = new JsonObjectRequest(
//                Request.Method.GET,
//                url,
//                null, //no body for this get request
//                mResponse::setValue,
//                this::handleError) {
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> headers = new HashMap<>();
//                // add headers <key,value>
//                headers.put("Authorization", jwt);
//                return headers;
//            }
//        };
//        request.setRetryPolicy(new DefaultRetryPolicy(
//                10_000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        //Instantiate the RequestQueue and add the request to the queue
//        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
//                .addToRequestQueue(request);
//    }

    /**
     * Gets the weather from the lat and longitude
     * @param jwt
     * @param lat the current latitude data
     * @param lon the current longitude data
     */
    public void connectLatLon(String jwt, double lat, double lon) {
        String url =
                "https://app-backend-server.herokuapp.com/weather/hourly/?&lat=" + lat + "&lon=" + lon;
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                mResponse::setValue,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }
    /**
     * Gets the forecast from the lat and longitude
     * @param jwt
     * @param lat the current latitude data
     * @param lon the current longitude data
     */
    public void connectLatLonForecast(String jwt, double lat, double lon) {
        String url =
                "https://app-backend-server.herokuapp.com/weather/daily/?&lat=" + lat + "&lon=" + lon;
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                mResponse::setValue,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    /**
     * This method will handle any error from the server.
     * @param error report from server.
     */
    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponse.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset())
                    .replace('\"', '\'');
            try {
                mResponse.setValue(new JSONObject("{" +
                        "code:" + error.networkResponse.statusCode +
                        ", data:\"" + data +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }
}
