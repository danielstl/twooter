package danstl.twooter.gui;

import danstl.twooter.JsonTwoot;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import twooter.Message;
import twooter.TwooterClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;

/**
 * Page for viewing information about a twoot, allowing following the author and searching for similar hashtags
 */
public class ViewTwootPage {

    private final Message message;
    private final TwooterClient client;

    private Stage stage;
    private TextArea twootText;
    private Image twootImage;

    public ViewTwootPage(Message message, TwooterClient client) {

        this.message = message;
        this.client = client;

        JsonTwoot json = JsonTwoot.resolve(message.message);

        stage = new Stage();

        BorderPane container = new BorderPane();

        twootText = new TextArea(json == null ? message.message : json.getText() == null ? "" : json.getText());
        twootText.setEditable(false);
        twootText.setWrapText(true);

        container.setCenter(twootText);

        container.setTop(new Text("Viewing Twoot"));

        if (json != null && json.getImages().length > 0) {
            String img = json.getImages()[0];

            img = img.split(",")[1];

            ImageView image = new ImageView(new Image(new ByteArrayInputStream(Base64.getDecoder().decode(img.getBytes()))));

            container.setLeft(image);
        }

        HBox bottom = new HBox();

        bottom.getChildren().add(new Text("Twoot Details"));
        bottom.getChildren().add(new Text("Published: " + message.published));

        Button followButton = new Button("Follow " + message.name);
        Button hashtagButton = new Button("View hashtags");

        ButtonBar bar = new ButtonBar();
        bar.getButtons().addAll(followButton, hashtagButton);

        bottom.getChildren().add(bar);

        container.setBottom(bottom);

        Scene scene = new Scene(container, 600, 400);

        stage.setScene(scene);
        stage.show();
    }
}
