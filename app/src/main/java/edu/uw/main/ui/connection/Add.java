package edu.uw.main.ui.connection;

import org.json.JSONException;
import org.json.JSONObject;

public class Add {

    private final String mUsername;

    public static class Builder {
        private String mUsername = "";



        public Builder(String username) {
            this.mUsername = username;
        }


        public Add build() {
            return new Add(this);
        }
    }

    /**
     * Constructor of connection post
     * @param builder the connection builder.
     */
    private Add(final Builder builder){
        this.mUsername = builder.mUsername;

    }
    /**
     * Static factory method to turn a properly formatted JSON String into a
     * ChatMessage object.
     * @param cmAsJson the String to be parsed into a ChatMessage Object.
     * @return a ChatMessage Object with the details contained in the JSON String.
     * @throws JSONException when cmAsString cannot be parsed into a ChatMessage.
     */
    public static Add createFromJsonString(final String cmAsJson) throws JSONException {

        return new Add(new Add.Builder(cmAsJson));

    }
    /**
     * Returns each connection.
     * @return
     */
    public String getUsername() {
        return mUsername;
    }

}

