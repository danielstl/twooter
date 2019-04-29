package danstl.twooter.gui;

import javafx.scene.control.ListCell;
import twooter.Message;

public class MessageCell extends ListCell<Message> {

    @Override
    protected void updateItem(Message item, boolean empty) {
        super.updateItem(item, empty);

        setText(item == null ? "Empty message" : item.name + "\n" + item.message);
    }
}
