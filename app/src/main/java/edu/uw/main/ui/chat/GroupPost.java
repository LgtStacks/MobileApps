package edu.uw.main.ui.chat;

import java.io.Serializable;
/**
 * The Group Post Card
 * @author Group 3
 * @version 6/2
 */
public class GroupPost implements Serializable {

    private String mChat = "";
    private int mchatID = -1;

    /**
     * Helper class for building Credentials.
     *
     * @author Charles Bryan
     */
    public static class Builder {
        private String mChat = "";
        private int mchatID = -1;



        public Builder(String chat, int id) {
            this.mChat = chat;
            this.mchatID = id;
        }


        public GroupPost build() {
            return new GroupPost(this);
        }
    }
    /**
     * default constructor.
     */
    public GroupPost(final Builder builder){
        this.mChat = builder.mChat;
        this.mchatID = builder.mchatID;
    }
    /**
     * Returns each Chat.
     * @return
     */
    public String getChat() {
        return mChat;
    }
    public int getId(){
        return mchatID;
    }
}
