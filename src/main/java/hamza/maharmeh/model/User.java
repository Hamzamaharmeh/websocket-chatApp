package hamza.maharmeh.model;

import java.util.UUID;

public class User {
    private final String username;
    private final String id;
    private final String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.id = UUID.randomUUID().toString();
    }

    public User(String username, String password, String id) {
        this.username = username;
        this.password = password;
        this.id = id;
    }

    public String username() {
        return username;
    }
    public String password() {
        return password;
    }
    public String id() {
        return id;
    }
}
