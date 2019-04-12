package danstl.twooter;

import danstl.twooter.gui.ChooseAccountPage;
import twooter.TwooterClient;

public class AccountManager {

    private TwooterClient client;
    private AccountDetails account;

    public AccountManager(TwooterClient client) {
        this.client = client;
    }

    public AccountDetails getAccount() {
        //todo check usersettings for existing name
        if (account != null) return account; //the user's already specified their name

        //we have to ask the user what name they want to use
        account = new ChooseAccountPage(client).getAccountDetails();

        return account;
    }
}
