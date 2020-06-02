package edu.uw.main.ui.chat;

public class Contacts {

    private final String mUsername;

    private final boolean checked;

    public static class Builder {
        private String mUsername = "";

        private boolean checked = false;


        public Builder(String username, boolean checked) {
            this.mUsername = username;
            this.checked = checked;
        }


        public Contacts build() {
            return new Contacts(this);
        }
    }

    /**
     * Constructor of connection post
     * @param builder the connection builder.
     */
    private Contacts(final Builder builder){
        this.mUsername = builder.mUsername;
        this.checked = builder.checked;

    }

    /**
     * Returns each connection.
     * @return
     */
    public String getUsername() {
        return mUsername;
    }
    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        checked = checked;
    }
}
