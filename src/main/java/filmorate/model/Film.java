package filmorate.model;

import filmorate.utils.validators.ReleaseDate;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Slf4j
public class Film {

    private @PositiveOrZero Long id;

    @NotBlank
    private String name;

    @Size(min = 1, max = 200, message = "Описание должно содержать от 1 до 200 символов")
    private String description;

    @ReleaseDate
    private LocalDate releaseDate;

    @Positive
    private int duration;

    private Set<Long> usersLike;

    public Film() {
        this.usersLike = new HashSet<Long>();
    }
}
