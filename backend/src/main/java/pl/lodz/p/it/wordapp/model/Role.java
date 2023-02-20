package pl.lodz.p.it.wordapp.model;

import java.util.stream.Stream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {
    READER(5),
    PROPOSER(10),
    EDITOR(15),
    OWNER(20);

    private final int value;

    public static Role of(int value) {
        return Stream.of(Role.values())
                     .filter(ar -> ar.value == value)
                     .findFirst()
                     .orElseThrow(IllegalArgumentException::new);
    }
}
