package danstl.twooter.gui;

import danstl.twooter.AccountDetails;
import danstl.twooter.AccountManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import twooter.Message;
import twooter.TwooterClient;

import java.io.IOException;

/**
 * Main page. Displays the twoot feed, contains a button to allow the user to post new twoots, and allows searching for
 * hashtags and specific users
 */
public class HomePage {

    private Button composeButton; //button to compose a new twoot
    private TextField hashtagSearchBox; //search box to search for users and hashtags

    private ListView<Message> messages; //the list of messages to display in the gui

    private TwooterClient client; //client reference for fetching messages
    private AccountDetails details; //the account details (username, token) for the specified user

    private PostFeed feed; //feed to automatically update the messages shown in the gui if not doing a search

    private Stage stage; //the window reference

    private boolean onlyShowFollowing; //true if only twoots from followed users should be shown

    public HomePage(TwooterClient client) {
        this.client = client;

        BorderPane container = new BorderPane();
        container.setPadding(new Insets(5));

        VBox topBar = new VBox();
        topBar.setPadding(new Insets(5));

        HBox header = new HBox();
        header.setSpacing(10);
        header.setAlignment(Pos.CENTER);

        topBar.getChildren().add(header);

        ToggleGroup feedToggle = new ToggleGroup();

        RadioButton allPosts = new RadioButton("All accounts");
        allPosts.setToggleGroup(feedToggle);
        allPosts.setUserData(false);
        allPosts.setSelected(true);

        RadioButton followingPosts = new RadioButton("Following only");
        followingPosts.setToggleGroup(feedToggle);
        followingPosts.setUserData(true);

        feedToggle.selectedToggleProperty().addListener((e, oldVal, newVal) -> {
            onlyShowFollowing = (boolean) newVal.getUserData();
            if (onlyShowFollowing) { //false for all accounts, true if for following only
                hashtagSearchBox.clear();
            }

            showAllTwoots();
        });

        HBox feedToggles = new HBox();
        feedToggles.setSpacing(10);
        feedToggles.getChildren().add(allPosts);
        feedToggles.getChildren().add(followingPosts);

        topBar.getChildren().add(feedToggles);

        container.setTop(topBar);

        Scene scene = new Scene(container, 1000, 800);

        details = new AccountManager(client).getAccount(); //will display a window allowing the user to pick their account name, or continue with their old one if existing

        composeButton = new Button("Compose Twoot");
        composeButton.setDisable(details == null); //do not allow the user to post if they are not logged in
        composeButton.setOnAction(e -> composeTwoot()); //call composeTwoot() when clicking the button

        header.getChildren().add(new Text(details == null ? "<Not logged in>" : details.getUserName()));

        header.getChildren().add(composeButton);

        hashtagSearchBox = new TextField();
        hashtagSearchBox.setPromptText("#hashtag / @username");

        hashtagSearchBox.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                doSearch(hashtagSearchBox.getText());
            }
        });

        header.getChildren().add(hashtagSearchBox);

        messages = new ListView<>();

        messages.setCellFactory(param -> new MessageCell());

        messages.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {

                Message selected = messages.getSelectionModel().getSelectedItem();

                new ViewTwootPage(selected, client, feed);
                //your code here
            }
        });

        feed = new PostFeed(client); //post feed handles fetching messages and live updates

        container.setCenter(messages);

        stage = new Stage();
        stage.setScene(scene);

        showAllTwoots();

        stage.show();
    }

    /**
     * Switches the messages feed to the live twoot feed
     */
    private void showAllTwoots() {
        if (onlyShowFollowing) {
            messages.setItems(feed.getFollowingMessages());
        } else {
            messages.setItems(feed.getMessages());
        }

        stage.setTitle("Twooter - all twoots");
    }

    /**
     * Attempts to either find a user using @syntax, or search for twoots with a specific tag with #syntax.
     * Other prefixes will result in an error message being displayed to the user
     *
     * If successful, the feed will be replaced with one which meets the users search criteria. For example only twoots
     * from a specific tag or user
     *
     * @param query the entire search query
     */
    private void doSearch(String query) {

        if (query.isEmpty() || query.equals("#") || query.equals("@")) { //if no search content has been entered, reset the feed
            showAllTwoots();
            return;
        }

        boolean hashtagSearch = query.startsWith("#");

        if (!hashtagSearch && !query.startsWith("@")) {
            Utils.showMessage(Alert.AlertType.ERROR, "Unknown search query. Use # to search for hashtags, and @ to search for a user's twoots");
            return;
        }

        ObservableList<Message> observableMsgs = FXCollections.observableArrayList(); //create a new observable list

        if (hashtagSearch) {
            String[] msgIds;

            try {
                msgIds = client.getTagged(query); //fetch message ids which match the query
            } catch (IOException ex) {
                Utils.showMessage(Alert.AlertType.ERROR, "Error fetching tweets for hashtag " + query);
                return;
            }

            if (msgIds == null || msgIds.length == 0) {
                Utils.showMessage(Alert.AlertType.WARNING, "No twoots were found matching this hashtag");
                return;
            }

            //fetch the actual twoots for each of the ids fetched above

            for (int i = 0; i < Math.min(20, msgIds.length); i++) { //only fetch up to 20 messages to avoid spamming requests
                try {
                    Message msg = client.getMessage(msgIds[i]);
                    observableMsgs.add(msg);
                } catch (Exception ex) {
                    System.out.println("Error fetching twoot:");
                    ex.printStackTrace();
                }
            }

        } else {
            try {
                Message[] msgs = client.getMessages(query.substring(1)); //remove the @, and fetch twoots from the user

                if (msgs == null || msgs.length == 0) {
                    Utils.showMessage(Alert.AlertType.WARNING, "No twoots were found from this user");
                    return;
                }

                observableMsgs.addAll(msgs);

            } catch (IOException ex) {
                Utils.showMessage(Alert.AlertType.ERROR, "Error fetching tweets for user " + query);
                return;
            }
        }

        messages.setItems(observableMsgs); //change to the new feed
        stage.setTitle("Twooter - " + query);
    }

    /**
     * Displays the twoot composition window
     */
    private void composeTwoot() {
        new ComposeTwootPage(details, client);
    }
}
