package filmorate.controller;

import filmorate.exceptions.filmExceptions.FilmNotFoundException;
import filmorate.exceptions.filmExceptions.GenreNotFoundException;
import filmorate.exceptions.filmExceptions.MpaNotFoundException;
import filmorate.exceptions.friendshipException.FriendshipAlreadyExistException;
import filmorate.exceptions.friendshipException.FriendshipNotFoundException;
import filmorate.exceptions.likeException.LikeNotFoundException;
import filmorate.exceptions.userExceptions.UserAlreadyInFriendsException;
import filmorate.exceptions.userExceptions.UserNotFoundException;
import filmorate.exceptions.userExceptions.UserNotFoundInFriendsException;
import filmorate.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.OK)
    public ErrorResponse handleFriendshipAlreadyExistError(final FriendshipAlreadyExistException exception) {
        log.error(String.format(exception.getMessage()), exception);
        return new ErrorResponse(
                "error",
                "Заявка в друзья уже отравлена"
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.OK)
    public ErrorResponse handleFriendshipNotFoundError(final FriendshipNotFoundException exception) {
        log.error(String.format(exception.getMessage()), exception);
        return new ErrorResponse(
                "error",
                "Заявка в друзья не найдена"
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundError(final UserNotFoundException exception) {
        log.error(String.format(exception.getMessage()), exception);
        return new ErrorResponse(
                "error",
                exception.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleLikeNotFoundError(final LikeNotFoundException exception) {
        log.error(String.format(exception.getMessage()), exception);
        return new ErrorResponse(
                "error",
                exception.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleMpaNotFoundError(final MpaNotFoundException exception) {
        log.error(String.format(exception.getMessage()), exception);
        return new ErrorResponse(
                "error",
                exception.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleGenreNotFoundError(final GenreNotFoundException exception) {
        log.error(String.format(exception.getMessage()), exception);
        return new ErrorResponse(
                "error",
                exception.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserAlreadyInFriendsError(final UserAlreadyInFriendsException exception) {
        log.error(String.format(exception.getMessage()), exception);
        return new ErrorResponse(
                "error",
                exception.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundInFriendsError(final UserNotFoundInFriendsException exception) {
        log.error(String.format(exception.getMessage()), exception);
        return new ErrorResponse(
                "error",
                exception.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmNotFoundError(final RuntimeException exception) {
        log.error(String.format(exception.getMessage()), exception);
        return new ErrorResponse(
                "error",
                exception.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmNotFoundError(final FilmNotFoundException exception) {
        log.error(String.format(exception.getMessage()), exception);
        return new ErrorResponse("error", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleDataIntegrityViolationError(final DataIntegrityViolationException exception) {
        log.error(String.format(exception.getMessage()), exception);
        return new ErrorResponse("error", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable exception) {
        log.error(String.format(exception.getMessage()), exception);
        return new ErrorResponse(
                "error",
                "Произошла непредвиденная ошибка."
        );
    }
}
