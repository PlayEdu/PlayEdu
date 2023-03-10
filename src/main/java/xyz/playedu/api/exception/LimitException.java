package xyz.playedu.api.exception;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/10 14:13
 */
public class LimitException extends Exception {
    public LimitException() {
    }

    public LimitException(String message) {
        super(message);
    }

    public LimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public LimitException(Throwable cause) {
        super(cause);
    }

    public LimitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
