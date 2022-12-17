package filmorate.controller;

import filmorate.model.ErrorResponse;
import filmorate.utils.exceptions.filmExceptions.FilmNotFoundException;
import filmorate.utils.exceptions.filmExceptions.GenreNotFoundException;
import filmorate.utils.exceptions.filmExceptions.MpaNotFoundException;
import filmorate.utils.exceptions.friendshipException.FriendshipAlreadyExistException;
import filmorate.utils.exceptions.friendshipException.FriendshipNotFoundException;
import filmorate.utils.exceptions.likeException.LikeNotFoundException;
import filmorate.utils.exceptions.userExceptions.UserAlreadyInFriendsException;
import filmorate.utils.exceptions.userExceptions.UserNotFoundException;
import filmorate.utils.exceptions.userExceptions.UserNotFoundInFriendsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

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
        log.error(String.format(Objects.requireNonNull(exception.getMessage())), exception);
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
