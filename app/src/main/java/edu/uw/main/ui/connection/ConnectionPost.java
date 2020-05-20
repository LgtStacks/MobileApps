package edu.uw.main.ui.connection;

import java.io.Serializable;
/**
 * The class to handle each connection post fragement.
 * @author Group 3
 * @version 5/19
 */
public class ConnectionPost implements Serializable {
    private final String mConnection;


    /**
     * Helper class for building Credentials.
     *
     * @author Charles Bryan
     */
    public static class Builder {
        private String mConnection = "";



        public Builder(String connection) {
            this.mConnection = connection;
        }


        public ConnectionPost build() {
            return new ConnectionPost(this);
        }
    }

    /**
     * Constructor of connection post
     * @param builder the connection builder.
     */
    private ConnectionPost(final Builder builder){
        this.mConnection = builder.mConnection;

    }

    /**
     * Returns each connection.
     * @return
     */
    public String getConnection() {
        return mConnection;
    }

}
