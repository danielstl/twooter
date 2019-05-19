package danstl.twooter.gui;

import danstl.twooter.JsonTwoot;
import javafx.scene.control.ListCell;
import twooter.Message;

/**
 * Message cell for formatting messages as they appear in the list on the main page feed
 */
public class MessageCell extends ListCell<Message> {

    /**
     * Method for displaying the message
     */
    @Override
    protected void updateItem(Message item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) { //clear the content if message is empty
            setText(null);
            return;
        }

        JsonTwoot json = JsonTwoot.resolve(item.message); //attempt to identify if this is a json twoot. Will be formatted differently if so

        if (json == null) { //a normal twoot
            setText(item.name + "\n" + item.message);
        } else {
            setText(item.name + (json.getImages() != null && json.getImages().length != 0 ? " - This twoot contains images" : "") + "\n" + (json.getText() == null ? "" : json.getText()) + "\n" + json.getAgent());
        }
    }
}
