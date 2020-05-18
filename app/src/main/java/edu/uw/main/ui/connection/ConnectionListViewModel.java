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
import java.util.function.IntFunction;

import edu.uw.main.R;

public class ConnectionListViewModel extends AndroidViewModel {
    private MutableLiveData<List<ConnectionPost>> mConnectionList;
    private MutableLiveData<List<ConnectionPost>> mUpdateList;

    public ConnectionListViewModel(@NonNull Application application) {
        super(application);
        mConnectionList = new MutableLiveData<>();
        mConnectionList.setValue(new ArrayList<>());
    }
    public void addConnectionListObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super List<ConnectionPost>> observer) {
        mConnectionList.observe(owner, observer);
    }
    private void handleError(final VolleyError error) {
        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
    }
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
    public void connectGet(final String jwt, String name) {
        JSONObject body = new JSONObject();
        try {
            body.put("email", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

}
