package danstl.twooter.gui;

import danstl.twooter.AccountDetails;
import twooter.TwooterClient;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ChooseAccountPage {

    private JDialog dialog;

    private JTextField userNameField;
    private JButton confirmButton;

    private TwooterClient client;

    private String selectedName, accountToken;

    public ChooseAccountPage(TwooterClient client) {
        this.client = client;

        dialog = new JDialog((JFrame) null, "Choose Your Account", true);

        dialog.setLayout(new GridLayout());

        userNameField = new JTextField();
        confirmButton = new JButton("Confirm");

        confirmButton.addActionListener(e -> checkName(userNameField.getText()));

        dialog.add(userNameField);
        dialog.add(confirmButton);

        dialog.setSize(300, 300);
        dialog.setVisible(true);
    }

    private void checkName(String name) {
        try {
            String token = client.registerName(userNameField.getText());

            if (token != null) {
                selectedName = name;
                accountToken = token;

                dialog.setVisible(false);
                dialog.dispose();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public AccountDetails getAccountDetails() {
        return new AccountDetails(selectedName, accountToken);
    }
}
