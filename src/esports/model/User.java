package esports.model;

public class User {
    protected String id;
    protected String username;
    protected String password;
    protected String fullName;
    protected String role;

    public User(String id, String username, String password, String fullName, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
    }

    public String getId()           { return id; }
    public String getUsername()     { return username; }
    public String getPassword()     { return password; }
    public String getFullName()     { return fullName; }
    public String getRole()         { return role; }
    public void setPassword(String p){ this.password = p; }
    public void setFullName(String n){ this.fullName = n; }

    @Override
    public String toString() { return fullName + " (" + username + ")"; }
}
