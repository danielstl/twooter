package danstl.twooter.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * General utility class for UI methods
 */
public final class Utils {

    /**
     * Displays a message window of the specified type
     * @param type the type of message to display (error, info etc.)
     * @param message the message to display
     */
    public static void showMessage(Alert.AlertType type, String message) {
        new Alert(type, message, ButtonType.OK).showAndWait();
    }
}
