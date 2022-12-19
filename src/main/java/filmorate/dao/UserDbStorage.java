package filmorate.dao;

import filmorate.model.User;
import filmorate.storage.interfaces.UserStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Objects;

@Component
@Primary
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("user_id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(LocalDate.parse(resultSet.getString("birthday"), DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build();
    }

    @Override
    public User create(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sqlQuery = "INSERT INTO users (email, login, name, birthday) " +
                "VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setString(4, user.getBirthday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            return stmt;
        }, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        return user;
    }

    @Override
    public User update(User user) throws EmptyResultDataAccessException {
        String sqlUpdateQuery = "UPDATE users " +
                "SET user_id = ?, email = ?, login = ?, name = ?, birthday = ? " +
                "WHERE user_id = ?";

        jdbcTemplate.update(sqlUpdateQuery,
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );

        return getById(user.getId());
    }

    @Override
    public Collection<User> getAll() {
        String sqlQuery = "SELECT user_id, email, login, name, birthday FROM users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User getById(Long id) throws EmptyResultDataAccessException {
        String sqlQuery = "SELECT user_id, email, login, name, birthday FROM users WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
    }

    public Collection<User> getFriends(Long userId) {
        String sqlQuery = "SELECT u.user_id, u.email, u.login, u.name, u.birthday " +
                "FROM users as u " +
                "JOIN user_friend AS uf " +
                "ON (u.user_id = uf.user_to AND uf.user_from = ?) " +
                "OR (u.user_id = uf.user_from AND uf.is_applied = true AND uf.user_to = ?)";

        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId, userId);
    }
}
