package aiss.dailymotionminer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE, reason = "Error al comunicarse con VideoMiner")
public class DailymotionServiceUnavailableException extends Exception {
    public DailymotionServiceUnavailableException() {
        super();
    }

    public DailymotionServiceUnavailableException(String message) {
        super(message);
    }

    public DailymotionServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
