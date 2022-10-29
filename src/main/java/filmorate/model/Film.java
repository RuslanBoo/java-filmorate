package filmorate.model;

import filmorate.utils.validators.ReleaseDate;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Slf4j
public class Film {

    @PositiveOrZero
    private int id;

    @NotBlank
    private String name;

    @Size(min = 1, max = 200, message = "Описание должно содержать от 1 до 200 символов")
    private String description;

    @ReleaseDate
    private LocalDate releaseDate;

    @Positive
    private int duration;
}
