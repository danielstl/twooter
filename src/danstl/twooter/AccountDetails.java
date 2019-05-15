package danstl.twooter;

import java.util.List;

public class AccountDetails {

    private final String userName;
    private final String token;

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
