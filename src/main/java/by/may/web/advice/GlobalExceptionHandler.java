package by.may.web.advice;

import by.may.domain.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException e) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Некорректные данные запроса");
        pd.setDetail("Ошибка валидации");

        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        pd.setDetail(message);
        return pd;
    }

    @ExceptionHandler({UserNotFoundException.class, CategoryNotFoundException.class, ProductNotFoundException.class, ReviewNotFoundException.class})
    public ProblemDetail handleNotFound(RuntimeException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(ReviewNotWrittenByUserException.class)
    public ProblemDetail handleForbidden(ReviewNotWrittenByUserException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler({UserAlreadyExistsException.class, CategoryAlreadyExistsException.class, UserAlreadyAddReviewForProduct.class})
    public ProblemDetail handleConflict(RuntimeException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler({CategoryNotEmpty.class, AdminMinLimitException.class, UserCannotDeleteYourselfException.class})
    public ProblemDetail handleBusinessRule(RuntimeException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT, e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleNotReadable (HttpMessageNotReadableException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied (AccessDeniedException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "Недостаточно прав");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuth (AuthenticationException  e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Требудет вход");
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected (Exception e) {
        log.error("Необработанная ошибка", e);
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка сервера");
    }
}
