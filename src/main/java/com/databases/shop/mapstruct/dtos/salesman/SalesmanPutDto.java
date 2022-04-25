package com.databases.shop.mapstruct.dtos.salesman;

import com.databases.shop.models.PersonName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import com.databases.shop.mapstruct.dtos.contacts.ContactsPutDto;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class SalesmanPutDto {

    @JsonProperty("full_name")
    @NotNull
    private PersonName personName;

    @JsonProperty("tel_nums")
    @NotNull
    private Set<TelephoneDto> telephones;

    @JsonProperty("dateOfBirth")
    @NotNull
    private Date dateOfBirth;

    @JsonProperty("dateOfHiring")
    @NotNull
    private Date dateOfHiring;

    @Override
    public String toString() {
        return "SalesmanPutDto{" +
                "personName=" + personName +
                ", telephones=" + telephones +
                ", dateOfBirth=" + dateOfBirth +
                ", dateOfHiring=" + dateOfHiring +
                '}';
    }
}
