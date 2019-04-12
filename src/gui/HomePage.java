package gui;

import twooter.TwooterClient;

import javax.swing.*;

public class HomePage {

    private JButton composeButton;

    public HomePage(TwooterClient client) {
        JFrame frame = new JFrame();

        frame.setTitle("Twooter");

        frame.add(new JLabel("Twooter"));

        composeButton = new JButton("Compose Twoot");
        composeButton.addActionListener(e -> composeTweet());

        frame.add(composeButton);

        frame.setSize(600, 400);

        frame.setVisible(true);
    }

    private void composeTweet() {
        System.out.println("todo");
    }
}
