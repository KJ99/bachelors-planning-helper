package pl.kj.bachelors.planning.domain.exception;

public class JwtInvalidException extends Exception {
    public JwtInvalidException() {
        super("JWT is not valid");
    }
    public JwtInvalidException(String message) {
        super(message);
    }
}
