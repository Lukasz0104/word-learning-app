package pl.lodz.p.it.wordapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ACCESS_ROLE")
@Getter
@Setter
public class AccessRole {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "ROLE")
    private Integer roleValue;

    @Transient
    private Role role;

    @ManyToOne
    @JoinColumn(name = "SET_ID")
    LearningSet set;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    Account user;

    @PrePersist
    private void fillPersistent() {
        roleValue = role.getValue();
    }

    @PostLoad
    private void fillTransient() {
        role = Role.of(roleValue);
    }
}
