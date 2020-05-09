package edu.uw.main.ui.connection;

import java.util.Arrays;
import java.util.List;

public final class ConnectionGenerator {
    private static final ConnectionPost[] FRIEND;
    public static final int COUNT = 10;


    static {
        FRIEND = new ConnectionPost[COUNT];
        FRIEND[0] = new ConnectionPost.Builder("Doom Slayer").build();
        FRIEND[1] = new ConnectionPost.Builder("Master Chief").build();
        FRIEND[2] = new ConnectionPost.Builder("Plankton").build();
        FRIEND[3] = new ConnectionPost.Builder("Charlie The Unicorn").build();
        FRIEND[4] = new ConnectionPost.Builder("Tyler").build();
        FRIEND[5] = new ConnectionPost.Builder("Mr.Krabs").build();
        FRIEND[6] = new ConnectionPost.Builder("Mega Mind").build();
        FRIEND[7] = new ConnectionPost.Builder("Clark Kent").build();
        FRIEND[8] = new ConnectionPost.Builder("Supreme Overlord").build();
        FRIEND[9] = new ConnectionPost.Builder("The Arbiter").build();

    }

    public static List<ConnectionPost> getBlogList() {
        return Arrays.asList(FRIEND);
    }

    public static ConnectionPost[] getBLOGS() {
        return Arrays.copyOf(FRIEND, FRIEND.length);
    }

    private ConnectionGenerator() { }
}
