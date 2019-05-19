package danstl.twooter.gui;

import danstl.twooter.AccountDetails;
import danstl.twooter.AccountManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import twooter.Message;
import twooter.TwooterClient;

import java.io.IOException;

public class HomePage {

    private Button composeButton;
    private Text accountLabel;
    private TextField hashtagSearchBox;

    private ListView<Message> messages;

    private TwooterClient client;
    private AccountDetails details;

    private PostFeed feed;

    private Stage stage;

    public HomePage(TwooterClient client) {
        this.client = client;

        BorderPane container = new BorderPane();
        container.setPadding(new Insets(5));

        HBox header = new HBox();
        header.setPadding(new Insets(5));
        header.setSpacing(10);
        header.setAlignment(Pos.CENTER);

        container.setTop(header);

        Scene scene = new Scene(container, 1000, 800);

        header.getChildren().add(new Text("Twooter"));

        composeButton = new Button("Compose Twoot");
        composeButton.setOnAction(e -> composeTwoot());

        header.getChildren().add(composeButton);

        details = new AccountManager(client).getAccount();

        accountLabel = new Text(details == null ? "<Not logged in>" : details.getUserName());

        header.getChildren().add(accountLabel);

        hashtagSearchBox = new TextField();
        hashtagSearchBox.setPromptText("#hashtag / @username");

        hashtagSearchBox.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                doHashtagSearch(hashtagSearchBox.getText());
            }
        });

        header.getChildren().add(hashtagSearchBox);

        messages = new ListView<>();

        messages.setCellFactory(param -> new MessageCell());

        messages.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {

                Message selected = messages.getSelectionModel().getSelectedItem();

                new ViewTwootPage(selected, client);
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

    private void showAllTwoots() {
        messages.setItems(feed.getMessages());

        stage.setTitle("Twooter - all twoots");
    }

    private void doHashtagSearch(String query) {

        if (query.isEmpty() || query.equals("#") || query.equals("@")) {
            showAllTwoots();
            return;
        }

        boolean hashtagSearch = query.startsWith("#");

        if (!hashtagSearch && !query.startsWith("@")) {
            Utils.showMessage(Alert.AlertType.ERROR, "Unknown search query. Use # to search for hashtags, and @ to search for a user's twoots");
            return;
        }

        ObservableList<Message> observableMsgs = FXCollections.observableArrayList();

        if (hashtagSearch) {
            String[] msgIds;

            try {
                msgIds = client.getTagged(query);
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
                Message[] msgs = client.getMessages(query.substring(1)); //remove the @
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

        messages.setItems(observableMsgs);
        stage.setTitle("Twooter - " + query);
    }

    private void composeTwoot() {
        new ComposeTwootPage(details, client);
    }
}
