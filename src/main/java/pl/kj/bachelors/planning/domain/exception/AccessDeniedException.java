package pl.kj.bachelors.planning.domain.exception;

public class AccessDeniedException extends Exception{
    protected final String message;

    public AccessDeniedException() {
        this("Access Denied");
    }

    public AccessDeniedException(String message) {
        this.message = message;
    }
}
