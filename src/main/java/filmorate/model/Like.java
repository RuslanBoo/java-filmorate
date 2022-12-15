package filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.PositiveOrZero;

@Data
@Builder
@Slf4j
public class Like {

    @NonNull
    @PositiveOrZero
    long filmId;

    @NonNull
    @PositiveOrZero
    long userId;
}
