package danstl.twooter;

import com.google.gson.Gson;
import danstl.twooter.gui.Utils;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class UserSettings {

    private static final String USER_SETTINGS_FILE = "settings.txt";
    private static final Gson GSON = new Gson();

    private static UserSettings instance = new UserSettings();

    public static UserSettings getInstance() {
        return instance;
    }

    private AccountDetails details;
    private List<String> followedAccounts;

    public UserSettings() {

    }

    public AccountDetails getDetails() {
        return details;
    }

    public void setDetails(AccountDetails details) {
        this.details = details;
        save();
    }

    public List<String> getFollowedAccounts() {
        return followedAccounts;
    }

    public void setFollowedAccounts(List<String> followedAccounts) {
        this.followedAccounts = followedAccounts;
        save();
    }

    public static void load() {
        Path settings = Paths.get(System.getProperty("user.dir"), USER_SETTINGS_FILE);

        if (Files.exists(settings)) {

            String data;

            try {
                data = new String(Files.readAllBytes(settings));
            } catch (IOException ex) {
                Utils.showMessage(Alert.AlertType.ERROR, "Error loading user settings");
                return;
            }

            instance = GSON.fromJson(data, UserSettings.class);
        }
    }

    private static void save() {
        Path settings = Paths.get(System.getProperty("user.dir"), USER_SETTINGS_FILE);

        try {
            Files.write(settings, GSON.toJson(instance).getBytes());
        } catch (IOException ex) {
            Utils.showMessage(Alert.AlertType.ERROR, "Error saving user settings");
        }
    }
}
