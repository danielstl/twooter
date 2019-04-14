package danstl.twooter.gui;

import danstl.twooter.AccountDetails;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
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

        StackPane container = new StackPane();

        userNameField = new TextField();
        confirmButton = new Button("Confirm");

        confirmButton.setOnAction(e -> checkName(userNameField.getText()));

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
