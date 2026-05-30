package rs.meningsistem.stamparija.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import rs.meningsistem.stamparija.exceptions.user.UserAlreadyExistException;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404), ex.getMessage());
        detail.setTitle("Resource not found");
        detail.setType(URI.create("urn:problem:resource-not-found"));
        decorate(detail, request);
        return detail;
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ProblemDetail handleUsernameNotFound(UsernameNotFoundException ex, HttpServletRequest request) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404), ex.getMessage());
        detail.setTitle("User not found");
        detail.setType(URI.create("urn:problem:user-not-found"));
        decorate(detail, request);
        return detail;
    }

    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail handleBadRequest(BadRequestException ex, HttpServletRequest request) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), ex.getMessage());
        detail.setTitle("Bad request");
        detail.setType(URI.create("urn:problem:bad-request"));
        decorate(detail, request);
        return detail;
    }

    @ExceptionHandler(ConflictException.class)
    public ProblemDetail handleConflict(ConflictException ex, HttpServletRequest request) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(409), ex.getMessage());
        detail.setTitle("Conflict");
        detail.setType(URI.create("urn:problem:conflict"));
        decorate(detail, request);
        return detail;
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ProblemDetail handleUserAlreadyExist(UserAlreadyExistException ex, HttpServletRequest request) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(409), ex.getMessage());
        detail.setTitle("User already exists");
        detail.setType(URI.create("urn:problem:user-already-exists"));
        decorate(detail, request);
        return detail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> validation = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                validation.put(err.getField(), err.getDefaultMessage()));

        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), "Greska pri validaciji");
        detail.setTitle("Validation failed");
        detail.setType(URI.create("urn:problem:validation"));
        detail.setProperty("validationErrors", validation);
        decorate(detail, request);
        return detail;
    }

    @ExceptionHandler({BadCredentialsException.class, AuthenticationException.class})
    public ProblemDetail handleAuthentication(Exception ex, HttpServletRequest request) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401),
                "Neispravno korisnicko ime ili lozinka");
        detail.setTitle("Unauthorized");
        detail.setType(URI.create("urn:problem:unauthorized"));
        decorate(detail, request);
        return detail;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403),
                "Nemate dozvolu za pristup ovom resursu");
        detail.setTitle("Forbidden");
        detail.setType(URI.create("urn:problem:forbidden"));
        decorate(detail, request);
        return detail;
    }

    @ExceptionHandler(AuthorizationServiceException.class)
    public ProblemDetail handleAuthorizationService(AuthorizationServiceException ex, HttpServletRequest request) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403),
                "Token nije validan ili je istekao");
        detail.setTitle("Authorization failed");
        detail.setType(URI.create("urn:problem:authorization"));
        decorate(detail, request);
        return detail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex, HttpServletRequest request) {
        log.error("Neocekivana greska", ex);
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500),
                "Doslo je do greske na serveru: " + ex.getMessage());
        detail.setTitle("Internal server error");
        detail.setType(URI.create("urn:problem:internal"));
        decorate(detail, request);
        return detail;
    }

    private void decorate(ProblemDetail detail, HttpServletRequest request) {
        detail.setProperty("timestamp", LocalDateTime.now());
        if (request != null) {
            detail.setProperty("path", request.getRequestURI());
        }
    }
}
