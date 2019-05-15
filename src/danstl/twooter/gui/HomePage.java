package danstl.twooter.gui;

import danstl.twooter.AccountDetails;
import danstl.twooter.AccountManager;
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
        hashtagSearchBox.setPromptText("#search");

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

        new PostFeed(messages.getItems(), client); //post feed handles fetching messages and live updates

        container.setCenter(messages);

        Stage stage = new Stage();
        stage.setTitle("Twooter");
        stage.setScene(scene);

        stage.show();
    }

    private void doHashtagSearch(String query) {
        if (!query.startsWith("#")) query = "#" + query;

        try {
            String[] msgs = client.getTagged(query);

            messages.getItems().clear();

            for (int i = 0; i < Math.min(10, msgs.length); i++) { //only fetch up to 10 messages to avoid spamming requests
                try {
                    Message msg = client.getMessage(msgs[i]);
                    messages.getItems().add(msg);
                } catch (Exception ignored) {
                }
            }

            Utils.showMessage(Alert.AlertType.INFORMATION, String.join(", ", msgs));
        } catch (IOException ex) {
            Utils.showMessage(Alert.AlertType.ERROR, "Error fetching tweets for " + query);
        }
    }

    private void composeTwoot() {
        new ComposeTwootPage(details, client);
    }
}
