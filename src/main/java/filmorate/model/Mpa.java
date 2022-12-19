package filmorate.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
public class Mpa {
    @NonNull
    @PositiveOrZero
    private Long id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
}
