package danstl.twooter.gui;

import danstl.twooter.AccountDetails;
import danstl.twooter.AccountManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import twooter.Message;
import twooter.TwooterClient;

import java.io.IOException;

public class HomePage {

    private Button composeButton;
    private Text accountLabel;

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

        Scene scene = new Scene(container, 600, 400);

        header.getChildren().add(new Text("Twooter"));

        composeButton = new Button("Compose Twoot");
        composeButton.setOnAction(e -> composeTwoot());

        header.getChildren().add(composeButton);

        details = new AccountManager(client).getAccount();

        accountLabel = new Text(details == null ? "<Not logged in>" : details.getUserName());

        header.getChildren().add(accountLabel);

        messages = new ListView<>();

        messages.setCellFactory(param -> new MessageCell());

        new PostFeed(messages.getItems(), client); //post feed handles fetching messages and live updates

        container.setCenter(messages);

        Stage stage = new Stage();
        stage.setTitle("Twooter");
        stage.setScene(scene);

        stage.show();
    }

    private void composeTwoot() {
        new ComposeTwootPage(details, client);
    }
}
