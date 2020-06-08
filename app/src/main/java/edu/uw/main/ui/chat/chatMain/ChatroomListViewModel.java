package edu.uw.main.ui.chat.chatMain;

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
import edu.uw.main.ui.chat.GroupPost;

/**
 * The Chatroom List View Model
 * @author Group 3
 * @version 6/2
 */
public class ChatroomListViewModel extends AndroidViewModel {
    private MutableLiveData<List<GroupPost>> mChatList;
    private MutableLiveData<List<GroupPost>> mUpdateList;

    /**
     * Chile Connection list view model.
     * @param application the application.
     */
    public ChatroomListViewModel(@NonNull Application application) {
        super(application);
        mChatList = new MutableLiveData<>();
        mChatList.setValue(new ArrayList<>());
    }

    /**
     * The connection list observer.
     * @param owner the owner
     * @param observer the observer.
     */
    public void addChatListObserver(@NonNull LifecycleOwner owner,
                                          @NonNull Observer<? super List<GroupPost>> observer) {
        mChatList.observe(owner, observer);
    }

    /**
     * Handles the error code when the server has trouble connecting.
     * @param error the server response error.
     */
    private void handleError(final VolleyError error) {
        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
    }

    /**
     * Handles the server response success code.
     * @param result the server response.
     */
    private void handleResult(final JSONObject result) {
        mUpdateList = new MutableLiveData<>();
        mUpdateList.setValue(new ArrayList<>());
        IntFunction<String> getString =
                getApplication().getResources()::getString;

        try {
            JSONArray messages = result.getJSONArray("rows");
            Log.e("Result CHECK", messages.toString());
            for(int i = 0; i < messages.length(); i++) {
                JSONObject message = messages.getJSONObject(i);
                mUpdateList.getValue().add(new GroupPost.Builder(message.getString("name"), message.getInt("chatid")).build());
            }
        }catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success ConnectionViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
        mChatList.setValue(mUpdateList.getValue());
    }

    /**
     * Sends a connection request to the web server.
     * @param jwt The Generated Java Web Token.
     */
    public void chatGet(final String jwt) {
        JSONObject body = new JSONObject();
        Log.e("BODY CHECK", body.toString());
        String url =
                "https://app-backend-server.herokuapp.com/chatRoom";
        Request request = new JsonObjectRequest(
                Request.Method.GET,
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
    public void chatDelete(final int id, final String jwt) {
        JSONObject body = new JSONObject();
        Log.e("BODY CHECK", body.toString());
        String url =
                "https://app-backend-server.herokuapp.com/chatRoom/"+id;
        Request request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                body,
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
