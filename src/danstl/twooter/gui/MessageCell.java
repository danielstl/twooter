package danstl.twooter.gui;

import danstl.twooter.JsonTwoot;
import javafx.scene.control.ListCell;
import twooter.Message;

public class MessageCell extends ListCell<Message> {

    @Override
    protected void updateItem(Message item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null) {
            setText("Empty message");
            return;
        }

        JsonTwoot json = JsonTwoot.resolve(item.message);

        if (json == null) { //a normal twoot
            setText(item.name + "\n" + item.message);
        } else {
            setText(item.name + (json.getImages().length != 0 ? " - This twoot contains images" : "") + "\n" + json.getText() + "\n" + json.getAgent());
        }
    }
}
