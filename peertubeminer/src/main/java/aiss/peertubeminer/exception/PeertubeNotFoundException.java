package aiss.peertubeminer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Recurso no encontrado en PeerTube")
public class PeertubeNotFoundException extends Exception {
    public PeertubeNotFoundException() {
        super();
    }

    public PeertubeNotFoundException(String message) {
        super(message);
    }

    public PeertubeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
