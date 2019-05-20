package danstl.twooter.gui;

import danstl.twooter.AccountDetails;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import twooter.TwooterClient;

import java.io.IOException;

/**
 * Page for allowing the user to compose new twoots. Contains a text input field and a submit button to post to the feed
 */
public class ComposeTwootPage {

    private Stage stage; //the window

    private TextArea textArea; //the text area, where the user writes the twoot
    private Button confirmButton; //button to submit the twoot

    private AccountDetails details; //the account details (name, token) to use to submit the twoot to the service
    private TwooterClient client; //the client to submit the twoot using

    public ComposeTwootPage(AccountDetails details, TwooterClient client) {
        this.details = details;
        this.client = client;

        stage = new Stage();

        BorderPane container = new BorderPane();
        container.setPadding(new Insets(10));

        Text twootLabel = new Text("What do you want to twoot?");

        textArea = new TextArea();
        confirmButton = new Button("Post");

        confirmButton.setOnAction(e -> publishTwoot(textArea.getText())); //publishTwoot with the text area text as the content parameter

        container.setTop(twootLabel);
        container.setCenter(textArea);
        container.setBottom(confirmButton);

        Scene scene = new Scene(container, 500, 300);
        stage.setTitle("Compose Twoot");
        stage.setScene(scene);

        stage.showAndWait();
    }

    /**
     * Publishes the twoot, after validation, to the system
     * @param content the twoot text
     */
    private void publishTwoot(String content) {
        if (content == null || content.isEmpty()) {
            Utils.showMessage(Alert.AlertType.ERROR, "Failed to post empty twoot!");
            return;
        }

        try {
            String res = client.postMessage(details.getToken(), details.getUserName(), content);
            Utils.showMessage(Alert.AlertType.INFORMATION, "Posted twoot!");
            System.out.println("Posted twoot: " + res);

        } catch (IOException ex) {
            Utils.showMessage(Alert.AlertType.ERROR, "Network error posting twoot!");
            ex.printStackTrace();
        }

        stage.close();
    }
}
