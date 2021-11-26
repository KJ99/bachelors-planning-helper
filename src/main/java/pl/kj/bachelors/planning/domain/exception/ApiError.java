package pl.kj.bachelors.planning.domain.exception;

public class ApiError extends Exception {
    private final String path;
    private final String message;
    private final String code;

    public ApiError(String message, String code, String path) {
        this.message = message;
        this.path = path;
        this.code = code;
    }

    public String getPath() {
        return path;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}
