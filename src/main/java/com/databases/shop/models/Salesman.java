package com.databases.shop.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Salesman {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Embedded
    private PersonName personName;

    @NotNull
    @OneToMany(mappedBy = "salesman", cascade = CascadeType.ALL)
    private Set<Telephone> telephones = new HashSet<>();

    @NotBlank
    @Column(name = "salesman_email", length = 100)
    private String email;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_birth", nullable = false)
    private Date dateOfBirth;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_hiring", nullable = false)
    private Date dateOfHiring;

    @OneToMany(mappedBy = "salesman")
    private Set<Order> orders = new HashSet<>();

}
