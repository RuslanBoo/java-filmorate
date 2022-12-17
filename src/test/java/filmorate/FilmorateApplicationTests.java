package filmorate;

import filmorate.model.Film;
import filmorate.model.Genre;
import filmorate.model.Mpa;
import filmorate.model.User;
import filmorate.service.interfaces.FilmService;
import filmorate.service.interfaces.GenreService;
import filmorate.service.interfaces.MpaService;
import filmorate.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateUserServiceTests {
    private final UserService userService;
    private final Long expectedUserId = 1L;

    @Test
    public void testCreateUser() {

        User user = User.builder()
                .name("Test user")
                .email("email@test.ru")
                .login("myNewLogin2022")
                .birthday(LocalDate.of(1990, 10, 10))
                .build();

        User createdUser = userService.create(user);
        assertThat(createdUser).isEqualTo(user);
    }

    @Test
    public void testFindUserById() {

        Optional<User> userOptional = Optional.ofNullable(userService.findById(expectedUserId));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", expectedUserId)
                );
    }

    @Test
    public void testUpdateUser() {

        User user = User.builder()
                .name("Test user")
                .email("email@test.ru")
                .login("myNewLogin2022")
                .birthday(LocalDate.of(1990, 10, 10))
                .build();

        User createdUser = userService.create(user);

        User updateUser = User.builder()
                .id(expectedUserId)
                .name("Changed Test user")
                .email("change_email@test.ru")
                .login("myNewOld2022")
                .birthday(LocalDate.of(1991, 11, 11))
                .build();

        User updatedUser = userService.update(updateUser);
        assertThat(updatedUser).isEqualTo(updateUser);
    }
}

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateFilmServiceTests {
    private final FilmService filmService;
    private final Long expectedFilmId = 1L;

    @Test
    public void testCreateFilm() {
        Film film = Film.builder()
                .id(expectedFilmId)
                .name("test")
                .description("test desc")
                .duration(120)
                .mpa(new Mpa(1L, "G"))
                .genres(new ArrayList<Genre>())
                .releaseDate(LocalDate.of(2010, 10 ,10))
                .build();

        Film createdFilm = filmService.createFilm(film);
        assertThat(createdFilm).isEqualTo(film);
    }

    @Test
    public void testFindFilmById() {

        Optional<Film> filmOptional = Optional.ofNullable(filmService.findFilmById(expectedFilmId));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", expectedFilmId)
                );
    }

    @Test
    public void testUpdateFilm() {

        Film film = Film.builder()
                .id(expectedFilmId)
                .name("test")
                .description("test desc")
                .duration(120)
                .mpa(new Mpa(1L, "G"))
                .genres(new ArrayList<Genre>())
                .releaseDate(LocalDate.of(2010, 10 ,10))
                .build();

        Film createdFilm = filmService.createFilm(film);

        Film updateFilm = Film.builder()
                .id(expectedFilmId)
                .name("test 2")
                .description("test desc 2")
                .duration(120)
                .mpa(new Mpa(1L, "G"))
                .genres(new ArrayList<Genre>())
                .releaseDate(LocalDate.of(2010, 11 ,11))
                .build();

        Film updatedFilm = filmService.updateFilm(updateFilm);
        assertThat(updatedFilm).isEqualTo(updateFilm);
    }
}

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateMpaServiceTests {
    private final MpaService mpaService;

    @Test
    public void testFindMpaById() {

        Optional<Mpa> mpaOptional = Optional.ofNullable(mpaService.findById(1L));

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("id", 1L)
                )
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("name", "G")
                );
    }
}

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateGenreServiceTests {
    private final GenreService genreService;

    @Test
    public void testFindGenreById() {

        Optional<Genre> filmGenre = Optional.ofNullable(genreService.findById(1L));

        assertThat(filmGenre)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("id", 1L)
                )
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("name", "Комедия")
                );
    }
}
