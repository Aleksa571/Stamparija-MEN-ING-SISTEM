package rs.meningsistem.stamparija.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object value) {
        super(String.format("%s sa %s '%s' nije pronadjen.", resourceName, fieldName, value));
    }
}
