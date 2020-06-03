package edu.uw.main.ui.connection;

import android.app.Application;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import androidx.lifecycle.ViewModelProvider;
import edu.uw.main.io.RequestQueueSingleton;

public class ConnectionAddViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mResponse;

    private MutableLiveData<List<Add>> mAddList;

    private MutableLiveData<List<Add>> mUpdateList;

    private ConnectionAddViewModel mAddModel;

    /**
     * Chile Connection list view model.
     *
     * @param application the application.
     */
    public ConnectionAddViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
        mAddList = new MutableLiveData<>();
        mAddList.setValue(new ArrayList<>());
    }
    /**
     * This method will add a response observer for interacting with the server.
     *
     * @param owner    Owner of the data.
     * @param observer JSON Object to report the result.
     */
    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    /**
     * The connection list observer.
     *
     * @param owner    the owner
     * @param observer the observer.
     */
    public void addConnectionAddObserver(@NonNull LifecycleOwner owner,
                                          @NonNull Observer<? super List<Add>> observer) {
        mAddList.observe(owner, observer);
    }



    /**
     * Handles the error code when the server has trouble connecting.
     *
     * @param error the server response error.
     */
    private void handleError(final VolleyError error) {
        Log.e("CONNECTION ERROR ADD", "CHECK FOR NULL VALUE IN INPUT");

      //  Log.e("CONNECTION ERROR", error.getLocalizedMessage());
      //  throw new IllegalStateException(error.getMessage());
    }

    /**
     * Sends email and password to our webservice. Authenticates the credentials.
     *
     * @param email - email the user is searching for.
     */
    public void connectSearch(final String email, final String jwt) {

        String url = "https://app-backend-server.herokuapp.com/connections/" + email;
        Log.e("URL ADD: ", email);
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handleResult,
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
     * Sends email and password to our webservice. Authenticates the credentials.
     *
     * @param email - email the user is searching for.
     */
    public void connectAdd(final String email, final String jwt) {
        Log.e("Add Step", "2");

        String url = "https://app-backend-server.herokuapp.com/connections/" + email;
        Request request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                null,
                null,
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
        Log.e("Add Step", "3");

    }

    /**
     * Handles the server response success code.
     *
     * @param response the server response.
     */
    private void handleResult(final JSONObject response) {
        Log.e("Add Step", "3.2");

        mUpdateList = new MutableLiveData<>();
        mUpdateList.setValue(new ArrayList<>());
        Log.e("SERVER RESPONSE", response.toString());
        try {
            JSONArray jsTemp = response.getJSONArray("email");
            int size = jsTemp.length();
            for (int i = 0; i < size; i++) {
                mUpdateList.getValue().add(new Add.Builder(jsTemp.getJSONObject(i).get("username").toString()).build());
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        mAddList.setValue(mUpdateList.getValue());
    }
}
