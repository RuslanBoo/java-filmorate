package filmorate.controller;

import filmorate.exceptions.filmExceptions.FilmNotFoundException;
import filmorate.exceptions.userExceptions.UserAlreadyInFriendsException;
import filmorate.exceptions.userExceptions.UserNotFoundException;
import filmorate.exceptions.userExceptions.UserNotFoundInFriendsException;
import filmorate.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundError(final UserNotFoundException exception) {
        log.error(String.format(exception.getMessage()));
        return new ErrorResponse(
                "error",
                exception.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserAlreadyInFriendsError(final UserAlreadyInFriendsException exception) {
        log.error(String.format(exception.getMessage()));
        return new ErrorResponse(
                "error",
                exception.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundInFriendsError(final UserNotFoundInFriendsException exception) {
        log.error(String.format(exception.getMessage()));
        return new ErrorResponse(
                "error",
                exception.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmNotFoundError(final RuntimeException exception) {
        log.error(String.format(exception.getMessage()));
        return new ErrorResponse(
                "error",
                exception.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmNotFoundError(final FilmNotFoundException exception) {
        log.error(String.format(exception.getMessage()));
        return new ErrorResponse("error", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable exception) {
        log.error(String.format(exception.getMessage()));
        return new ErrorResponse(
                "error",
                "Произошла непредвиденная ошибка."
        );
    }
}
