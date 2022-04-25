package com.databases.shop.mapstruct.dtos.salesman;

import com.databases.shop.models.Contacts;
import com.databases.shop.models.PersonName;
import com.databases.shop.models.Telephone;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@ToString
public class SalesmanSaveDto {

    @JsonProperty("full_name")
    @NotNull
    private PersonName personName;

    @JsonProperty("tel_nums")
    @NotNull
    private Set<TelephoneDto> telephones;

    @JsonProperty("email")
    @NotNull
    private String email;

    @JsonProperty("dateOfBirth")
    @NotNull
    private Date dateOfBirth;

    @JsonProperty("dateOfHiring")
    @NotNull
    private Date dateOfHiring;
}
