package edu.uw.main.ui.connection;

public class Sent {

    private final String mUsername;

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
     * Returns each connection.
     * @return
     */
    public String getUsername() {
        return mUsername;
    }

}

