package com.databases.shop.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@Embeddable
@Getter
@Setter
public class PersonName {

    @NotBlank
    @Max(15)
    @Column(name = "person_name", length = 15)
    private String name;

    @Column(name = "person_lastname", length = 15)
    @Max(15)
    private String lastname;

    @NotBlank
    @Column(name = "person_surname", length = 15)
    @Max(15)
    private String surname;

    public PersonName(@NotBlank @Max(15) String name, @Max(15) String lastname, @NotBlank @Max(15) String surname) {
        this.name = name;
        this.lastname = lastname;
        this.surname = surname;
    }

    public PersonName() {
    }
}
