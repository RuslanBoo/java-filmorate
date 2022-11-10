package filmorate.controller;

import filmorate.exceptions.userExceptions.UserAlreadyInFriendsException;
import filmorate.exceptions.userExceptions.UserNotFoundException;
import filmorate.exceptions.userExceptions.UserNotFoundInFriendsException;
import filmorate.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundError(final UserNotFoundException e) {
        return new ErrorResponse(
                "error",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserAlreadyInFriendsError(final UserAlreadyInFriendsException e) {
        return new ErrorResponse(
                "error",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundInFriendsError(final UserNotFoundInFriendsException e) {
        return new ErrorResponse(
                "error",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmNotFoundError(final RuntimeException e) {
        return new ErrorResponse(
                "error",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        return new ErrorResponse(
                "error",
                "Произошла непредвиденная ошибка."
        );
    }
}
