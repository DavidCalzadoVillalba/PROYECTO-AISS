package aiss.dailymotionminer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Recurso no encontrado en Dailymotion")
public class DailymotionNotFoundException extends Exception {
    public DailymotionNotFoundException() {
        super();
    }

    public DailymotionNotFoundException(String message) {
        super(message);
    }

    public DailymotionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
