package filmorate;

import filmorate.dao.UserDbStorage;
import filmorate.model.User;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final Long expectedUserId = 1L;

    @Test
    public void testCreateUser() {

        User user = User.builder()
                .name("Test user")
                .email("email@test.ru")
                .login("myNewLogin2022")
                .birthday(LocalDate.of(1990, 10, 10))
                .build();

        User createdUser = userStorage.create(user);
        assertThat(createdUser).isEqualTo(user);
    }

    @Test
    public void testFindUserById() {

        Optional<User> userOptional = Optional.ofNullable(userStorage.findById(expectedUserId));

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

        User createdUser = userStorage.create(user);

        User updateUser = User.builder()
                .id(expectedUserId)
                .name("Changed Test user")
                .email("change_email@test.ru")
                .login("myNewOld2022")
                .birthday(LocalDate.of(1991, 11, 11))
                .build();

        User updatedUser = userStorage.update(updateUser);
        assertThat(updatedUser).isEqualTo(updateUser);
    }
}
