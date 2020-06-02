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

import edu.uw.main.io.RequestQueueSingleton;

public class ConnectionSentViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mResponse;

    private MutableLiveData<List<Sent>> mSentList;

    private MutableLiveData<List<Sent>> mUpdateList;

    /**
     * Chile Connection list view model.
     *
     * @param application the application.
     */
    public ConnectionSentViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
        mSentList = new MutableLiveData<>();
        mSentList.setValue(new ArrayList<>());
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
    public void addSentObserver(@NonNull LifecycleOwner owner,
                                   @NonNull Observer<? super List<Sent>> observer) {
        mSentList.observe(owner, observer);
    }

    /**
     * Handles the error code when the server has trouble connecting.
     *
     * @param error the server response error.
     */
    private void handleError(final VolleyError error) {
        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
    }

    /**
     * Sends email and password to our webservice. Authenticates the credentials.
     *
     */
    public void connectGetSentRequests(final String jwt) {

        String url = "https://app-backend-server.herokuapp.com/contacts/";
        Request request = new JsonObjectRequest(
                Request.Method.POST,
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

//    /**
//     * Sends email and password to our webservice. Authenticates the credentials.
//     *
//     * @param email - email the user is searching for.
//     */
//    public void connectGetRequest(final String email, final String jwt) {
//
//        String url = "https://app-backend-server.herokuapp.com/contacts/" + email;
//        Request request = new JsonObjectRequest(
//                Request.Method.GET,
//                url,
//                null,
//                null,
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
    public void addSentItem( final String username) {

    mUpdateList = new MutableLiveData<>();
    mUpdateList.setValue(mSentList.getValue());
    mUpdateList.getValue().add(new Sent.Builder(username).build());

    mSentList.setValue(mUpdateList.getValue());
    // Log.e("MODIFIED ADD LIST: ", mAddList.getValue().get(mAddList.getValue().size() -1 ).getUsername());

}
    public void removeSentItem(final String username){
    int size = mSentList.getValue().size();
    mUpdateList = new MutableLiveData<>();
    mUpdateList.setValue(mSentList.getValue());

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
            mSentList = new MutableLiveData<>();
            mSentList.setValue(new ArrayList<>());
        } else{
            mSentList.setValue(mUpdateList.getValue());
        }
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
                mUpdateList.getValue().add(new Sent.Builder(jsTemp.getJSONObject(i).get("email").toString()).build());
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        mSentList.setValue(mUpdateList.getValue());
    }
}

