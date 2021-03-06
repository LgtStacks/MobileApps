package edu.uw.main.ui.connection.connectionPending;

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

import edu.uw.main.io.RequestQueueSingleton;

/**
 * The class to handle the recycler view adapter.
 * @author Group 3
 * @version 6/2
 */
public class ConnectionPendingViewModel extends AndroidViewModel {
    private MutableLiveData<JSONObject> mResponse;

    private MutableLiveData<List<Pending>> mPendingList;

    private MutableLiveData<List<Pending>> mUpdateList;

    private ConnectionPendingViewModel mPendingModel;

    /**
     * Chile Connection list view model.
     *
     * @param application the application.
     */
    public ConnectionPendingViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
        mPendingList = new MutableLiveData<>();
        mPendingList.setValue(new ArrayList<>());
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
    public void addPendingObserver(@NonNull LifecycleOwner owner,
                                         @NonNull Observer<? super List<Pending>> observer) {
        mPendingList.observe(owner, observer);
    }

    /**
     * Adds a new Pending Request
     * @param username the user
     */
    public void addPendingRequest( final String username) {

        mUpdateList = new MutableLiveData<>();
        mUpdateList.setValue(mPendingList.getValue());
        mUpdateList.getValue().add(new Pending.Builder(username).build());

        mPendingList.setValue(mUpdateList.getValue());
        // Log.e("MODIFIED ADD LIST: ", mAddList.getValue().get(mAddList.getValue().size() -1 ).getUsername());

    }

    /**
     * Removes a pending request.
     * @param username the user
     */
    public void removePendingRequest(final String username){
        int size = mPendingList.getValue().size();
        mUpdateList = new MutableLiveData<>();
        mUpdateList.setValue(mPendingList.getValue());
        Log.e("PENDING LIST SIZE: ", String.valueOf(size));
        Log.e("PENDING LIST FIRST CONTENT: ", mPendingList.getValue().get(0).getUsername());
        boolean checkEmpty = false;
        for(int i = 0; i < size; i++){
            String check = mUpdateList.getValue().get(i).getUsername();
            if(check.equals(username)){
                mUpdateList.getValue().remove(i);

                break;
            }
        }
        size = mUpdateList.getValue().size();
        if(size == 0){
            Log.e("PATH CHECK: ", "TRUE EMPTY");
            mPendingList = new MutableLiveData<>();
            mPendingList.setValue(new ArrayList<>());
        } else{
            mPendingList.setValue(mUpdateList.getValue());
        }
        Log.e("PENDING LIST OUTPUT: ", mPendingList.getValue().toString());

    }
    /**
     * Handles the error code when the server has trouble connecting.
     *
     * @param error the server response error.
     */
    private void handleError(final VolleyError error) {
       // Log.e("CONNECTION ERROR", error.getLocalizedMessage());
     //   throw new IllegalStateException(error.getMessage());
    }

    /**
     * Sends email and password to our webservice. Authenticates the credentials.
     *
     * @param jwt - email the user is searching for.
     */
    public void connectGetPendingRequests(final String jwt) {

        String url = "https://app-backend-server.herokuapp.com/contacts/";
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
     * @param jwt - email the user is searching for.
     */
    public void connectAccept(final String email, final String jwt) {

        String url = "https://app-backend-server.herokuapp.com/contacts/" + email;
        Request request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                null, //no body for this get request
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
    }
    /**
     * Sends email and password to our webservice. Authenticates the credentials.
     *
     * @param jwt - email the user is searching for.
     */
    public void connectDecline(final String email, final String jwt) {

        String url = "https://app-backend-server.herokuapp.com/contacts/" + email;
        Request request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null, //no body for this get request
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
    }

    /**
     * Handles the server response success code.
     *
     * @param response the server response.
     */
    private void handleResult(final JSONObject response) {
        mUpdateList = new MutableLiveData<>();
        mUpdateList.setValue(new ArrayList<>());
        try {
            JSONArray jsTemp = response.getJSONArray("email");
            int size = jsTemp.length();
            for (int i = 0; i < size; i++) {
                //Could be bugged due to incorrect process.
                mUpdateList.getValue().add(new Pending.Builder(jsTemp.getJSONObject(i).get("email").toString()).build());
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        mPendingList.setValue(mUpdateList.getValue());
    }
}
