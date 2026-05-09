package aiss.peertubeminer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE, reason = "Error al comunicarse con VideoMiner")
public class PeertubeServiceUnavailableException extends Exception {
    public PeertubeServiceUnavailableException() {
        super();
    }

    public PeertubeServiceUnavailableException(String message) {
        super(message);
    }

    public PeertubeServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
