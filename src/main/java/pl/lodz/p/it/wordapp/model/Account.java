package pl.lodz.p.it.wordapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ACCOUNT")
@Getter
@Setter
@ToString
public class Account {

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "USERNAME", unique = true)
    private String userName;

    @Column(name = "EMAIL", unique = true)
    private String emailAddress;

    @ToString.Exclude
    @JsonIgnore
    @Column(name = "PASSWORD", nullable = false)
    private String password;

}