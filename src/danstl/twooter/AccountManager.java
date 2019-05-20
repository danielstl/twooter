package danstl.twooter;

import danstl.twooter.gui.ChooseAccountPage;
import twooter.TwooterClient;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AccountManager {

    private TwooterClient client;
    private AccountDetails account;

    public AccountManager(TwooterClient client) {
        this.client = client;
    }

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
