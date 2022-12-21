package antoni.nawrocki.models;

public class UserModel {
    private String id;
    private String username;
    private String login;
    private String password;
    private boolean isAdmin;
    private boolean isCompany;
    private String profilePicture;

    public UserModel(String id, String username, String login, String password, boolean isAdmin, boolean isCompany, String profilePicture) {
        this.id = id;
        this.username = username;
        this.login = login;
        this.password = password;
        this.isAdmin = isAdmin;
        this.isCompany = isCompany;
        this.profilePicture = profilePicture;
    }

    public UserModel(String username, String login, String password, boolean isAdmin, boolean isCompany, String profilePicture) {
        this.id = null;
        this.username = username;
        this.login = login;
        this.password = password;
        this.isAdmin = isAdmin;
        this.isCompany = isCompany;
        this.profilePicture = profilePicture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isCompany() {
        return isCompany;
    }

    public void setCompany(boolean company) {
        isCompany = company;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
