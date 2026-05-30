package rs.meningsistem.stamparija.exceptions;

public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object value) {
        super(String.format("%s sa %s '%s' nije pronadjen.", resourceName, fieldName, value));
    }
}
