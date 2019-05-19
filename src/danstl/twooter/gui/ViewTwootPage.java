package danstl.twooter.gui;

import danstl.twooter.JsonTwoot;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import twooter.Message;
import twooter.TwooterClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Page for viewing information about a twoot, allowing following the author and searching for similar hashtags
 */
public class ViewTwootPage {

    private static final DateTimeFormatter TWEET_TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"); //timestamp format for twoot publish time

    private final Message message; //the twoot to be displayed
    private final TwooterClient client; //client reference

    private Stage stage; //the window
    private TextArea twootText; //content of the twoot
    private Image twootImage; //base64 image to display, if the twoot is formatted using json

    /**
     * Creates and displays the twoot viewer window. Displays the twoot and additional information about a single twoot
     * @param message the twoot to display
     * @param client the client reference
     */
    public ViewTwootPage(Message message, TwooterClient client) {

        this.message = message;
        this.client = client;

        JsonTwoot json = JsonTwoot.resolve(message.message); //attempts to create a json twoot from the message, for images and other metadata

        stage = new Stage();
        stage.setTitle("Viewing Twoot " + message.id);

        BorderPane container = new BorderPane();

        twootText = new TextArea(json == null ? message.message : json.getText() == null ? "" : json.getText());
        twootText.setEditable(false); //don't allow editing of the message
        twootText.setWrapText(true);

        container.setPadding(new Insets(5));

        container.setCenter(twootText);

        container.setTop(new Text("Viewing Twoot"));

        //Images, if twoot is formatted in json format
        if (json != null && json.getImages().length > 0) {
            try {
                String img = json.getImages()[0];

                img = img.split(",")[1];

                ImageView image = new ImageView(new Image(new ByteArrayInputStream(Base64.getDecoder().decode(img.getBytes()))));

                container.setLeft(image);
            } catch (Exception ignored) {
                container.setLeft(new Text("Malformed image")); //the json isnt formatted in the correct base64 format
            }
        }

        HBox bottom = new HBox();

        VBox details = new VBox();

        //Twoot details
        details.getChildren().add(new Text("Twoot Details"));
        details.getChildren().add(new Text("Published: " + TWEET_TIMESTAMP_FORMAT.format(Instant.ofEpochMilli(message.published).atZone(ZoneId.systemDefault()).toLocalDateTime())));
        details.getChildren().add(new Text("Author: " + message.name));

        bottom.getChildren().add(details);

        //Follow user button
        Button followButton = new Button("+ Follow " + message.name);
        followButton.setMinWidth(200);
        bottom.getChildren().add(followButton);

        container.setBottom(bottom);

        Scene scene = new Scene(container, 600, 400);

        stage.setScene(scene);
        stage.show(); //display the window, don't wait because the user should be able to interact with other twoots in the background
    }
}
