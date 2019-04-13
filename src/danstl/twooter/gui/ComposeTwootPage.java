package danstl.twooter.gui;

import danstl.twooter.AccountDetails;
import twooter.TwooterClient;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ComposeTwootPage {

    private JDialog dialog;
    private JTextArea textArea;
    private JButton confirmButton;

    private AccountDetails details;
    private TwooterClient client;

    public ComposeTwootPage(AccountDetails details, TwooterClient client) {
        this.details = details;
        this.client = client;

        dialog = new JDialog((JFrame) null, "Compose Twoot", true);

        dialog.setLayout(new GridLayout());

        textArea = new JTextArea();
        confirmButton = new JButton("Post");

        confirmButton.addActionListener(e -> publishTwoot(textArea.getText()));

        dialog.add(textArea);
        dialog.add(confirmButton);

        dialog.setSize(500, 300);
        dialog.setVisible(true);
    }

    private void publishTwoot(String content) {
        if (content == null || content.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Failed to post empty twoot!");
            return;
        }

        try {
            String res = client.postMessage(details.getToken(), details.getUserName(), content);
            JOptionPane.showMessageDialog(null, "Posted twoot! " + res);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Network error posting twoot!");
            ex.printStackTrace();
        }

        dialog.setVisible(false);
    }
}
