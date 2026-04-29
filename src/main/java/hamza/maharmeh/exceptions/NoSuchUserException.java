package hamza.maharmeh.exceptions;

public class NoSuchUserException extends RuntimeException {
    private String user;
    public NoSuchUserException(String user) {
        this.user = user;
    }
    public String user() {
        return user;
    }
}
