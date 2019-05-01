package danstl.twooter;

import java.util.List;

public class AccountDetails {

    private final String userName;
    private final String token;
    private List<String> followedAccounts;

    public AccountDetails(String userName, String token, List<String> followedAccounts) {

        this.userName = userName;
        this.token = token;
        this.followedAccounts = followedAccounts;
    }

    public String getUserName() {
        return userName;
    }

    public String getToken() {
        return token;
    }

    public List<String> getFollowedAccounts() {
        return followedAccounts;
    }
}
