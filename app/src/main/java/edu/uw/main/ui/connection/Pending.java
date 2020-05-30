package edu.uw.main.ui.connection;

public class Pending {

    private final String mUsername;

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
     * Returns each connection.
     * @return
     */
    public String getUsername() {
        return mUsername;
    }

}

