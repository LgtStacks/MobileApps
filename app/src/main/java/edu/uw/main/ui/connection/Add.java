package edu.uw.main.ui.connection;

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
     * Returns each connection.
     * @return
     */
    public String getUsername() {
        return mUsername;
    }

}

