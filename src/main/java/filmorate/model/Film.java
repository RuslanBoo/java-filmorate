package filmorate.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import filmorate.utils.validators.ReleaseDate;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@Slf4j
public class Film {
    @PositiveOrZero
    private Long id;

    @NonNull
    @NotBlank
    private String name;

    @NonNull
    @Size(min = 1, max = 200, message = "Описание должно содержать от 1 до 200 символов")
    private String description;

    @NonNull
    @ReleaseDate
    private LocalDate releaseDate;

    @NonNull
    @Positive
    private int duration;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Mpa mpa;

    private List<Genre> genres;
}
