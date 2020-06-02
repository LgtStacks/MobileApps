package edu.uw.main.ui.connection;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntFunction;

import edu.uw.main.R;
/**
 * A List view model for each connection.
 * @author Group 3
 * @version 5/19
 */
public class ConnectionListViewModel extends AndroidViewModel {
    private MutableLiveData<List<ConnectionPost>> mConnectionList;
    private MutableLiveData<List<ConnectionPost>> mUpdateList;

    /**
     * Chile Connection list view model.
     * @param application the application.
     */
    public ConnectionListViewModel(@NonNull Application application) {
        super(application);
        mConnectionList = new MutableLiveData<>();
        mConnectionList.setValue(new ArrayList<>());
    }

    /**
     * The connection list observer.
     * @param owner the owner
     * @param observer the observer.
     */
    public void addConnectionListObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super List<ConnectionPost>> observer) {
        mConnectionList.observe(owner, observer);
    }

    /**
     * Handles the error code when the server has trouble connecting.
     * @param error the server response error.
     */
    private void handleError(final VolleyError error) {
      //  Log.e("CONNECTION ERROR", Objects.requireNonNull(error.getLocalizedMessage()));
      //  throw new IllegalStateException(error.getMessage());
    }
    public void addFriend( final String username) {

        mUpdateList = new MutableLiveData<>();
        mUpdateList.setValue(mConnectionList.getValue());
        mUpdateList.getValue().add(new ConnectionPost.Builder(username).build());

        mConnectionList.setValue(mUpdateList.getValue());
        // Log.e("MODIFIED ADD LIST: ", mAddList.getValue().get(mAddList.getValue().size() -1 ).getUsername());

    }
    public void removeFriend(final String username){
        int size = mConnectionList.getValue().size();
        mUpdateList = new MutableLiveData<>();
        mUpdateList.setValue(mConnectionList.getValue());
        Log.e("PENDING LIST SIZE: ", String.valueOf(size));
        Log.e("PENDING LIST FIRST CONTENT: ", mConnectionList.getValue().get(0).getConnection());
        boolean checkEmpty = false;
        for(int i = 0; i < size; i++){
            String check = mUpdateList.getValue().get(i).getConnection();
            if(check.equals(username)){
                mUpdateList.getValue().remove(i);

                break;
            }
        }
        size = mUpdateList.getValue().size();
        if(size == 0){
            mConnectionList = new MutableLiveData<>();
            mConnectionList.setValue(new ArrayList<>());
        } else {
            mConnectionList.setValue(mUpdateList.getValue());
        }
    }
    /**
     * Handles the server response success code.
     * @param result the server response.
     */
    private void handleResult(final JSONObject result) {
        Log.e("Result CHECK", result.toString());
        mUpdateList = new MutableLiveData<>();
        mUpdateList.setValue(new ArrayList<>());
        IntFunction<String> getString =
                getApplication().getResources()::getString;
        try {
            JSONArray messages = result.getJSONArray("email");
            for(int i = 0; i < messages.length(); i++) {
                JSONObject message = messages.getJSONObject(i);
                mUpdateList.getValue().add(new ConnectionPost.Builder(
                        message.getString(
                                getString.apply(R.string.keys_json_connection_email)))
                .build());
            }
        }catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success ConnectionViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
        mConnectionList.setValue(mUpdateList.getValue());
    }

    /**
     * Sends a connection request to the web server.
     * @param jwt The Generated Java Web Token.
     */
    public void connectGet(final String jwt) {
        JSONObject body = new JSONObject();
        Log.e("BODY CHECK", body.toString());
        String url =
                "https://app-backend-server.herokuapp.com/connections";
        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
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
        Log.e("REQUEST CHECK: ", request.toString());
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    /**
     * Sends a connection request to the web server.
     * @param jwt The Generated Java Web Token.
     */
    public void connectDelete(final String jwt, final String toDelete) {
        JSONObject body = new JSONObject();
        String url =
                "https://app-backend-server.herokuapp.com/connections/" + toDelete;
        Request request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                body,
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
        Log.e("REQUEST CHECK: ", request.toString());
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

}
