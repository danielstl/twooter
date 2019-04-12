public class UserSettings {

    private String userName;

    public UserSettings() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        save();
    }

    private void load() {

    }

    private void save() {

    }
}
