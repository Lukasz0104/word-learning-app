package pl.lodz.p.it.wordapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ACCESS_ROLE")
@Getter
@Setter
@NoArgsConstructor
public class AccessRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public AccessRole(Role role, LearningSet ls, Account acc) {
        this.role = role;
        this.set = ls;
        this.user = acc;
    }

    @PrePersist
    private void fillPersistent() {
        roleValue = role.getValue();
    }

    @PostLoad
    private void fillTransient() {
        role = Role.of(roleValue);
    }
}
