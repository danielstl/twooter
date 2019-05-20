package danstl.twooter;

import danstl.twooter.gui.ChooseAccountPage;
import twooter.TwooterClient;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class for managing the user's account. Will attempt to access the existing details and show this in the account
 * picker UI if they have an existing name, otherwise will show the normal name picker window
 *
 * Also in charge of refreshing the account token
 */
public class AccountManager {

    private TwooterClient client; //the client to use for refreshing token and displaying the account picker window
    private AccountDetails account; //the user's account details

    /**
     * Creates an account manager
     * @param client the twooter client instance
     */
    public AccountManager(TwooterClient client) {
        this.client = client;
    }

    /**
     * Will check if the user's account details are already saved. If so, it will return them. Otherwise, it will
     * display the account picker window with the option of using the user's previous name if that is stored in
     * the settings.txt user settings file
     *
     * @return the account details
     */
    public AccountDetails getAccount() {
        if (account != null) return account; //the user's already specified their name

        AccountDetails existingDetails = UserSettings.getInstance().getDetails();

        //we have to ask the user what name they want to use
        account = new ChooseAccountPage(client, existingDetails).getAccountDetails();

        enableAutoRefresh();

        return account;
    }

    /**
     * Enables the auto-refresh of the user's account token.
     * Happens every minute
     */
    public void enableAutoRefresh() {
        ScheduledExecutorService exe = Executors.newSingleThreadScheduledExecutor();

        exe.scheduleAtFixedRate(() -> {
            try {
                System.out.println("Refreshing account token...");
                if (account != null) client.refreshName(account.getUserName(), account.getToken());
            } catch (IOException ex) {
                System.out.println("Error refreshing account token");
                ex.printStackTrace();
            }
        }, 1, 1, TimeUnit.MINUTES); //refresh the name every minute
    }
}
