package danstl.twooter.gui;

import danstl.twooter.AccountDetails;
import danstl.twooter.AccountManager;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import twooter.TwooterClient;

import javax.swing.*;
import java.awt.*;

public class HomePage {

    private Button composeButton;
    private Text accountLabel;

    private TwooterClient client;
    private AccountDetails details;

    public HomePage(TwooterClient client) {
        this.client = client;

        new PostFeed(client);

        StackPane container = new StackPane();

        Scene scene = new Scene(container,600, 400);

        container.getChildren().add(new Text("Twooter"));

        composeButton = new Button("Compose Twoot");
        composeButton.setOnAction(e -> composeTweet());

        container.getChildren().add(composeButton);

        details = new AccountManager(client).getAccount();

        accountLabel = new Text(details.getUserName());

        container.getChildren().add(accountLabel);

        Stage stage = new Stage();
        stage.setTitle("Twooter");
        stage.setScene(scene);

        stage.show();
    }

    private void composeTweet() {
        new ComposeTwootPage(details, client);
    }
}
