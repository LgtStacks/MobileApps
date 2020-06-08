package edu.uw.main.ui.connection.connectionSent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class to help build the sent users.
 */
public class Sent {

    private final String mUsername;

    /**
     * Helper Builder Class
     */
    public static class Builder {
        private String mUsername = "";



        public Builder(String username) {
            this.mUsername = username;
        }


        public Sent build() {
            return new Sent(this);
        }
    }

    /**
     * Constructor of connection post
     * @param builder the connection builder.
     */
    private Sent(final Builder builder){
        this.mUsername = builder.mUsername;

    }
    /**
     * Static factory method to turn a properly formatted JSON String into a
     * ChatMessage object.
     * @param cmAsJson the String to be parsed into a ChatMessage Object.
     * @return a ChatMessage Object with the details contained in the JSON String.
     * @throws JSONException when cmAsString cannot be parsed into a ChatMessage.
     */
    public static Sent createFromJsonString(final String cmAsJson) throws JSONException {
        final JSONObject msg = new JSONObject(cmAsJson);
        // return new Pending(msg.getString("message"));
        return new Sent(new Sent.Builder(msg.getString("message")));

    }
    /**
     * Returns each connection.
     * @return
     */
    public String getUsername() {
        return mUsername;
    }

}

