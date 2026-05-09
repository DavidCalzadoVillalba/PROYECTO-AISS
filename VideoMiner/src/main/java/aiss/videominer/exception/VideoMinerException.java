package aiss.videominer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Recurso no encontrado")
public class VideoMinerException extends Exception {
    public VideoMinerException() {
        super();
    }

    public VideoMinerException(String message) {
        super(message);
    }

    public VideoMinerException(String message, Throwable cause) {
        super(message, cause);
    }
}
