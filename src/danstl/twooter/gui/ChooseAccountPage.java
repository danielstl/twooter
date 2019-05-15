package danstl.twooter.gui;

import danstl.twooter.AccountDetails;
import danstl.twooter.UserSettings;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import twooter.TwooterClient;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Class for the account picker window. Will allow the user to pick a username if there is not already existing details saved
 */
public class ChooseAccountPage {

    private Stage stage; //the window to display

    private TextField userNameField; //the field that the user enters their name into
    private Button confirmButton; //the button to submit and attempt to verify the name
    private Button useExistingButton; //the button to use the user's previous account details

    private TwooterClient client; //client reference to validate the name and get the token

    private AccountDetails accountDetails; //the supplied account details
    private AccountDetails existingAccountDetails; //the account details which the user previously used

    /**
     * Creates and displays the account picker window
     * @param client the TwooterClient reference for fetching the token
     */
    public ChooseAccountPage(TwooterClient client, AccountDetails existingDetails) {
        this.client = client;
        this.existingAccountDetails = existingDetails;

        stage = new Stage();

        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setSpacing(10);
        container.setPadding(new Insets(10));

        userNameField = new TextField();
        confirmButton = new Button("Confirm");

        confirmButton.setOnAction(this::onConfirm);

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

        if (existingDetails != null) {
            useExistingButton = new Button("Continue with " + existingDetails.getUserName());
            useExistingButton.setOnAction(this::onContinueWithExisting);

            container.getChildren().add(useExistingButton);
        }

        Scene scene = new Scene(container, 300, 300);
        stage.setTitle("Choose Your Account");
        stage.setScene(scene);

        stage.showAndWait();
    }

    /**
     * Checks if the specified account name is valid to use
     *
     * @param name the name to check if it is valid. If so, it will be assigned to the accountDetails variable
     * @return true if the name is valid (not taken and the right length), false otherwise
     */
    private boolean checkName(String name) {
        try {
            String token = client.registerName(userNameField.getText());

            if (token != null) {
                accountDetails = new AccountDetails(name, token);
                UserSettings.getInstance().setDetails(accountDetails);

                return true;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public AccountDetails getAccountDetails() {
        return accountDetails;
    }

    /**
     * Event fired when clicking on the 'confirm' button
     */
    private void onConfirm(ActionEvent e) {
        if (!checkName(userNameField.getText())) { //name is invalid
            Utils.showMessage(Alert.AlertType.ERROR, "Invalid name specified. It may be taken, or the name is too long or short");
        } else { //name is valid
            stage.close(); //hide the window, will then display the homepage
        }
    }

    /**
     * Event fired when continuing with the user's previous account details
     */
    private void onContinueWithExisting(ActionEvent e) {
        boolean success = false;

        try {
            success = client.refreshName(existingAccountDetails.getUserName(), existingAccountDetails.getToken());
        } catch (IOException ex) {
            ex.printStackTrace();;
        }

        if (!success) { //couldn't refresh the existing name, might've been taken
            Utils.showMessage(Alert.AlertType.ERROR, "Failed to refresh existing user: " + existingAccountDetails.getUserName());
        } else {
            accountDetails = existingAccountDetails;
            stage.close(); //hide the window, will then display the homepage
        }
    }
}
