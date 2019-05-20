package danstl.twooter;

import danstl.twooter.gui.HomePage;
import javafx.application.Application;
import javafx.stage.Stage;
import twooter.TwooterClient;

/**
 * JavaFX entrypoint for the Twooter application
 */
public class TwooterApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        TwooterClient c = new TwooterClient();

        UserSettings.load(); //attempt to load the user's previous settings, such as account name/token and followed accounts if it exists

        new HomePage(c); //show the main window, which in turn will open the account picker first
    }
}
