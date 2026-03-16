package esports.model;

public class Admin extends User {
    public Admin(String id, String username, String password, String fullName) {
        super(id, username, password, fullName, "ADMIN");
    }
}
