package pl.lodz.p.it.wordapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "learning_set_item")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@IdClass(LearningSetItemKey.class)
public class LearningSetItem {

    @Id
    @Column(nullable = false)
    private Long setID;

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long itemID;

    private String term;

    private String translation;

}