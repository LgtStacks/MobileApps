package edu.uw.main.ui.connection;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * The class to handle the Pending Cards.
 * @author Group 3
 * @version 6/2
 */
public class Pending {

    private final String mUsername;

    /**
     * Helper Build Class
     */
    public static class Builder {
        private String mUsername = "";



        public Builder(String username) {
            this.mUsername = username;
        }


        public Pending build() {
            return new Pending(this);
        }
    }

    /**
     * Constructor of connection post
     * @param builder the connection builder.
     */
    private Pending(final Builder builder){
        this.mUsername = builder.mUsername;

    }
    /**
     * Static factory method to turn a properly formatted JSON String into a
     * ChatMessage object.
     * @param cmAsJson the String to be parsed into a ChatMessage Object.
     * @return a ChatMessage Object with the details contained in the JSON String.
     * @throws JSONException when cmAsString cannot be parsed into a ChatMessage.
    */
    public static Pending createFromJsonString(final String cmAsJson) throws JSONException {
        Log.e("PENDING FRONT INPUT: ", cmAsJson);

        return new Pending(new Pending.Builder(cmAsJson));

    }


    /**
     * Returns each connection.
     * @return
     */
    public String getUsername() {
        return mUsername;
    }

}

