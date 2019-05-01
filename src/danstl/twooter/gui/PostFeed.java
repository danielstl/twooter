package danstl.twooter.gui;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import twooter.Message;
import twooter.TwooterClient;
import twooter.TwooterEvent;
import twooter.UpdateListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Twooter post feed, handles fetching messages and updating when new messages are received.
 * Implements the Twooter UpdateListener interface to listen for messages
 */
public class PostFeed implements UpdateListener {

    private ObservableList<Message> messages; //the observablelist to update when we get new twoots
    private TwooterClient client; //the client to hook the update listener to

    /**
     * Creates a post feed listener
     * @param messagesList the list to update when receiveing new messages
     * @param client the client instance
     */
    public PostFeed(ObservableList<Message> messagesList, TwooterClient client) {
        this.client = client;

        client.enableLiveFeed(); //to allow the updates to work
        client.addUpdateListener(this);

        messages = messagesList;

        try {
            messages.addAll(client.getMessages()); //to make mutable
        } catch (IOException ex) {
            System.err.println("Error fetching messages");
            ex.printStackTrace();
        }
    }

    /**
     * Handles the TwooterEvent. Filters for message events and adds them to the messages list
     */
    @Override
    public void handleUpdate(TwooterEvent e) {
        System.out.println("ev:");
        System.out.println(e.payload);
        System.out.println(e.type);
        System.out.println(e.time);

        if (e.type == TwooterEvent.MESSAGE) {
            System.out.println("New message: " + e.payload);

            try {
                Message msg = client.getMessage(e.payload);
                if (msg == null) return;
                Platform.runLater(() -> messages.add(0, msg)); //run on the fx application thread to stop exception
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
