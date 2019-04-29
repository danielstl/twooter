package danstl.twooter.gui;

import danstl.twooter.AccountDetails;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import twooter.TwooterClient;

import java.io.IOException;

public class ChooseAccountPage {

    private Stage stage;

    private TextField userNameField;
    private Button confirmButton;

    private TwooterClient client;

    private String selectedName, accountToken;

    public ChooseAccountPage(TwooterClient client) {
        this.client = client;

        stage = new Stage();

        VBox container = new VBox();
        container.setSpacing(10);
        container.setPadding(new Insets(10));

        userNameField = new TextField();
        confirmButton = new Button("Confirm");

        confirmButton.setOnAction(e -> checkName(userNameField.getText()));

        Text welcomeText = new Text("Welcome to Twooter");
        welcomeText.setTextAlignment(TextAlignment.CENTER);
        welcomeText.setFont(Font.font(22f));

        Text nameText = new Text("Choose your name");
        nameText.setTextAlignment(TextAlignment.CENTER);
        nameText.setFont(Font.font(20f));

        container.getChildren().add(welcomeText);
        container.getChildren().add(nameText);
        container.getChildren().add(userNameField);
        container.getChildren().add(confirmButton);

        Scene scene = new Scene(container, 300, 300);
        stage.setTitle("Choose Your Account");
        stage.setScene(scene);

        stage.showAndWait();
    }

    private void checkName(String name) {
        try {
            String token = client.registerName(userNameField.getText());

            if (token != null) {
                selectedName = name;
                accountToken = token;

                stage.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public AccountDetails getAccountDetails() {
        return new AccountDetails(selectedName, accountToken);
    }
}
