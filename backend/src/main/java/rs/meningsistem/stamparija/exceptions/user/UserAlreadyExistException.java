package rs.meningsistem.stamparija.exceptions.user;

import rs.meningsistem.stamparija.exceptions.BaseException;

public class UserAlreadyExistException extends BaseException {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
