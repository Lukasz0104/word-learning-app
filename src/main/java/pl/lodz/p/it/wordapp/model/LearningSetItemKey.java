package pl.lodz.p.it.wordapp.model;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class LearningSetItemKey implements Serializable {
    private Long setID;
    private Long itemID;
}
