package edu.uw.main.ui.chat;
/**
 * The Contacts Card Post.
 * @author Group 3
 * @version 6/2
 */
public class Contacts {

    private final String mUsername;

    private final boolean checked;

    /**
     * Contact Card Builder.
     */
    public static class Builder {
        private String mUsername = "";

        private boolean checked = false;

        /**
         * The Card Builder
         * @param username The name of the user
         * @param checked Check statement.
         */
        public Builder(String username, boolean checked) {
            this.mUsername = username;
            this.checked = checked;
        }

        /**
         * Builds up a new Contact
         * @return a new contact
         */
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

    /**
     * Returns Check Value
     * @return check
     */
    public boolean getChecked() {
        return checked;
    }

    /**
     * Sets Checked Value.
     * @param checked boolean check value
     */
    public void setChecked(boolean checked) {
        checked = checked;
    }
}
