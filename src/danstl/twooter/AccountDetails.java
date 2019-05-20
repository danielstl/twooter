package danstl.twooter;

import java.util.List;

/**
 * Class for storing the mapping between a user's name and their account token, used for publishing twoots and
 * account authentication
 */
public class AccountDetails {

    private final String userName; //the user/display name for the account
    private final String token; //the internal token for authentication

    /**
     * Creates a new AccountDetails instance
     * @param userName the user name
     * @param token internal token
     */
    public AccountDetails(String userName, String token) {

        this.userName = userName;
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public String getToken() {
        return token;
    }
}
