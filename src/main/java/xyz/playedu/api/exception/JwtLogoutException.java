package xyz.playedu.api.exception;

public class JwtLogoutException extends Exception {
    public JwtLogoutException() {
        super();
    }

    public JwtLogoutException(String message) {
        super(message);
    }

    public JwtLogoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtLogoutException(Throwable cause) {
        super(cause);
    }

    protected JwtLogoutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
