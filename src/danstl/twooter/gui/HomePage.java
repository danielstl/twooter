package danstl.twooter.gui;

import danstl.twooter.AccountDetails;
import danstl.twooter.AccountManager;
import twooter.TwooterClient;

import javax.swing.*;
import java.awt.*;

public class HomePage {

    private JButton composeButton;
    private JLabel accountLabel;

    private AccountDetails details;

    public HomePage(TwooterClient client) {
        JFrame frame = new JFrame();

        frame.setTitle("Twooter");
        frame.setLayout(new GridLayout());

        frame.add(new JLabel("Twooter"));

        composeButton = new JButton("Compose Twoot");
        composeButton.addActionListener(e -> composeTweet());

        frame.add(composeButton);

        details = new AccountManager(client).getAccount();

        accountLabel = new JLabel(details.getUserName());

        frame.add(accountLabel);

        frame.setSize(600, 400);

        frame.setVisible(true);
    }

    private void composeTweet() {
        System.out.println("todo");
    }
}
