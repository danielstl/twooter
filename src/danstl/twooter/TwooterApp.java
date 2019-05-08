package danstl.twooter;

import danstl.twooter.gui.HomePage;
import javafx.application.Application;
import javafx.stage.Stage;
import twooter.TwooterClient;

public class TwooterApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        TwooterClient c = new TwooterClient();

        UserSettings.load();

        new HomePage(c);
    }
}
