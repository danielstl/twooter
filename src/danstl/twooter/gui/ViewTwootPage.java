package danstl.twooter.gui;

import danstl.twooter.JsonTwoot;
import danstl.twooter.UserSettings;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Set;

/**
 * Page for viewing information about a twoot, allowing following the author and searching for similar hashtags
 */
public class ViewTwootPage {

    private static final DateTimeFormatter TWEET_TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"); //timestamp format for twoot publish time

    private final Message message; //the twoot to be displayed
    private final TwooterClient client; //client reference

    private Stage stage; //the window
    private Image twootImage; //base64 image to display, if the twoot is formatted using json

    /**
     * Creates and displays the twoot viewer window. Displays the twoot and additional information about a single twoot
     *
     * @param message the twoot to display
     * @param client  the client reference
     */
    public ViewTwootPage(Message message, TwooterClient client) {

        this.message = message;
        this.client = client;

        JsonTwoot json = JsonTwoot.resolve(message.message); //attempts to create a json twoot from the message, for images and other metadata

        stage = new Stage();
        stage.setTitle("Viewing Twoot " + message.id);

        BorderPane container = new BorderPane();

        VBox twootContent = new VBox();
        twootContent.setAlignment(Pos.CENTER);
        twootContent.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 3px;");

        if ((json != null && (json.getText() == null || json.getText().isEmpty())) || (message.message == null || message.message.isEmpty())) {
            Text emptyText = new Text("This twoot does not contain any content");
            emptyText.setStyle("-fx-font-style: italic");
            twootContent.getChildren().add(emptyText);
        } else {
            TextArea twootText = new TextArea(json == null ? message.message : json.getText() == null ? "" : json.getText());
            twootText.setEditable(false); //don't allow editing of the message
            twootText.setWrapText(true);

            //makes the text take up the full width and height available
            twootText.prefWidthProperty().bind(twootContent.widthProperty());
            twootText.prefHeightProperty().bind(twootContent.heightProperty());

            twootContent.getChildren().add(twootText);
        }

        container.setPadding(new Insets(5));

        HBox header = new HBox();
        header.setPadding(new Insets(5));
        header.getChildren().add(new Text("Viewing Twoot"));

        container.setTop(header);

        //Images, if twoot is formatted in json format
        ImageView img = getImage(json);

        if (img != null) {
            twootContent.getChildren().add(0, img); //add to the start to display above the twoot text content
        }

        container.setCenter(twootContent);

        HBox bottom = new HBox();
        bottom.setPadding(new Insets(5));

        bottom.setSpacing(20);
        bottom.setAlignment(Pos.CENTER_LEFT);

        VBox details = new VBox();

        //Twoot details
        Text twootDetailsHeader = new Text("Twoot Details");
        twootDetailsHeader.setStyle("-fx-font-weight: 800");
        details.getChildren().add(twootDetailsHeader);
        details.getChildren().add(new Text("Published: " + TWEET_TIMESTAMP_FORMAT.format(Instant.ofEpochMilli(message.published).atZone(ZoneId.systemDefault()).toLocalDateTime())));
        details.getChildren().add(new Text("Author: " + message.name));

        if (json != null && json.getAgent() != null) {
            details.getChildren().add(new Text("Agent: " + json.getAgent()));
        }

        bottom.getChildren().add(details);

        //Follow user button
        Button followButton = new Button("+ Follow " + message.name);
        followButton.setOnAction(this::followUser);
        followButton.setMinWidth(200);
        bottom.getChildren().add(followButton);

        container.setBottom(bottom);

        Scene scene = new Scene(container, 600, 400);

        stage.setScene(scene);
        stage.show(); //display the window, don't wait because the user should be able to interact with other twoots in the background
    }

    /**
     * Attempts to fetch the first image from the json twoot to display in the UI.
     * Will be null if there are no images
     *
     * @param jsonTwoot the json twoot to get the image from
     * @return the first image in the twoot, or null if not present
     */
    private ImageView getImage(JsonTwoot json) {
        if (json == null || json.getImages() == null || json.getImages().length == 0) return null;

        try {
            String img = json.getImages()[0];

            img = img.split(",")[1]; //split the header off of the base64 code
            ImageView image = new ImageView(new Image(new ByteArrayInputStream(Base64.getDecoder().decode(img.getBytes()))));

            return image;

        } catch (Exception ignored) {
            return null; //the json isnt formatted in the correct base64 format
        }
    }

    /**
     * The method to be called when clicking on the follow user button. Will add the user to the followed users list in the user settings
     *
     * @param e the event passed via lambda
     */
    private void followUser(ActionEvent e) {

        Set<String> following = UserSettings.getInstance().getFollowedAccounts();

        if (following.add(message.name)) { //attempt to follow, will return false if already following (so unfollow instead)
            Utils.showMessage(Alert.AlertType.INFORMATION, "Now following @" + message.name);
        } else {
            following.remove(message.name);
            Utils.showMessage(Alert.AlertType.INFORMATION, "Now unfollowing @" + message.name);
        }
    }
}
