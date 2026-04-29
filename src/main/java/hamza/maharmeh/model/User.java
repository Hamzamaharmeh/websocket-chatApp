package hamza.maharmeh.model;

import java.util.UUID;

public class User {
    private final String username;
    private final String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String username() {
        return username;
    }
    public String password() {
        return password;
    }

    public boolean equals(Object o) {
        if(o == null) return false;
        if(o == this) return true;
        if(!(o instanceof User u)) return false;
        return this.username.equals(u.username) && this.password.equals(u.password);
    }

    public static String encodePassword(String password) {
        //TODO should encode the input using the encoding algorithm
        return password;
    }
}
