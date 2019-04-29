package danstl.twooter.gui;

import twooter.Message;
import twooter.TwooterClient;
import twooter.TwooterEvent;
import twooter.UpdateListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PostFeed implements UpdateListener {

    private List<Message> messages;

    public PostFeed(TwooterClient client) {
        client.enableLiveFeed();
        client.addUpdateListener(this);

        try {
            messages = new ArrayList<>(Arrays.asList(client.getMessages()));
        } catch (IOException ex) {
            System.err.println("Error fetching messages");
            ex.printStackTrace();
        }
    }

    @Override
    public void handleUpdate(TwooterEvent e) {
        System.out.println("ev:");
        System.out.println(e.payload);
        System.out.println(e.type);
        System.out.println(e.time);

        if (e.type == TwooterEvent.MESSAGE) {
            System.out.println("New message: " + e.payload);
        }
    }
}
