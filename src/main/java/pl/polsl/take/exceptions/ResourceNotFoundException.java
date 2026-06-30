package pl.polsl.take.exceptions;

// server internal error 500
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
}