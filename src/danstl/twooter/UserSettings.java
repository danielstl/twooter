package danstl.twooter;

import com.google.gson.Gson;
import danstl.twooter.gui.Utils;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class for storing the user's account details and followed accounts persistently using the JSON format.
 *
 * Data is stored in a settings.txt file, and is read and parsed by GSON and then saved back to when the contents change
 */
public class UserSettings {

    private static final String USER_SETTINGS_FILE = "settings.txt"; //file name to store the settings to/read from
    private static final Gson GSON = new Gson(); //GSON instance for saving and reading json

    private static UserSettings instance = new UserSettings(); //singleton instance

    /**
     * @return the singleton instance of the user's settings
     */
    public static UserSettings getInstance() {
        return instance;
    }

    private AccountDetails details; //the account details (username, token)
    private List<String> followedAccounts; //the accounts ths user follows

    /**
     * @return the account details
     */
    public AccountDetails getDetails() {
        return details;
    }

    /**
     * Sets the account details and then saves to the disk
     * @param details the account details
     */
    public void setDetails(AccountDetails details) {
        this.details = details;
        save();
    }

    /**
     * @return the accounts the user follows. If null, will be instantiated first
     */
    public List<String> getFollowedAccounts() {
        if (followedAccounts == null) followedAccounts = new ArrayList<>();

        return followedAccounts;
    }

    /**
     * Sets the following info and then saves to disk
     * @param followedAccounts the accounts ths user follows
     */
    public void setFollowedAccounts(List<String> followedAccounts) {
        this.followedAccounts = followedAccounts;
        save();
    }

    /**
     * Loads the file from disk and then parses with json to instantiate the singleton instance.
     * If the file does not exist, or the file is invalid, the default instance is used
     */
    public static void load() {
        Path settings = Paths.get(System.getProperty("user.dir"), USER_SETTINGS_FILE);

        if (Files.exists(settings)) {

            String data;

            try {
                data = new String(Files.readAllBytes(settings)); //read the file as a string
            } catch (IOException ex) {
                Utils.showMessage(Alert.AlertType.ERROR, "Error loading user settings");
                return;
            }

            instance = GSON.fromJson(data, UserSettings.class); //parse the string through json
        }
    }

    /**
     * Saves the settings to disk via json. Parsed through gson
     */
    public static void save() {
        Path settings = Paths.get(System.getProperty("user.dir"), USER_SETTINGS_FILE);

        try {
            Files.write(settings, GSON.toJson(instance).getBytes()); //write the json encoded version of user settings
        } catch (IOException ex) {
            Utils.showMessage(Alert.AlertType.ERROR, "Error saving user settings");
        }
    }
}
