package danstl.twooter.gui;

import danstl.twooter.AccountDetails;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import twooter.TwooterClient;

import java.io.IOException;

public class ComposeTwootPage {

    private Stage stage;

    private TextArea textArea;
    private Button confirmButton;

    private AccountDetails details;
    private TwooterClient client;

    public ComposeTwootPage(AccountDetails details, TwooterClient client) {
        this.details = details;
        this.client = client;

        stage = new Stage();

        BorderPane container = new BorderPane();
        container.setPadding(new Insets(10, 10, 10, 10));

        Text twootLabel = new Text("What do you want to twoot?");

        textArea = new TextArea();
        confirmButton = new Button("Post");

        confirmButton.setOnAction(e -> publishTwoot(textArea.getText()));

        container.setTop(twootLabel);
        container.setCenter(textArea);
        container.setBottom(confirmButton);

        Scene scene = new Scene(container, 500, 300);
        stage.setTitle("Compose Twoot");
        stage.setScene(scene);

        stage.showAndWait();
    }

    private void publishTwoot(String content) {
        if (content == null || content.isEmpty()) {
            Utils.showMessage(Alert.AlertType.ERROR, "Failed to post empty twoot!");
            return;
        }

        try {
            String res = client.postMessage(details.getToken(), details.getUserName(), content);
            Utils.showMessage(Alert.AlertType.INFORMATION, "Posted twoot! " + res);

        } catch (IOException ex) {
            Utils.showMessage(Alert.AlertType.ERROR, "Network error posting twoot!");
            ex.printStackTrace();
        }

        stage.close();
    }
}
