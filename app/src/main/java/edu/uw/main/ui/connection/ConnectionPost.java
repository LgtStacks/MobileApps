package edu.uw.main.ui.connection;

import java.io.Serializable;

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
    private ConnectionPost(final Builder builder){
        this.mConnection = builder.mConnection;

    }


    public String getConnection() {
        return mConnection;
    }

}
