package com.databases.shop.mapstruct.dtos.user;

import com.databases.shop.mapstruct.dtos.salesman.TelephoneDto;
import com.databases.shop.models.Address;
import com.databases.shop.models.Contacts;
import com.databases.shop.models.PersonName;
import com.databases.shop.models.Telephone;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@ToString
@Setter
@Getter
public class UserGetDto {

    @JsonProperty("id")
    @NotNull
    private Long id;

    @JsonProperty("full_name")
    @NotNull
    private PersonName personName;

    @JsonProperty("contacts")
    @NotNull
    private Contacts contacts;

    @JsonProperty("email")
    @NotNull
    private String email;

    @JsonProperty("tel_nums")
    @NotNull
    private Set<TelephoneDto> telephones;

    @JsonProperty("address")
    @Nullable
    private Address address;

    @JsonProperty("dateOfBirth")
    @NotNull
    private Date dateOfBirth;

    @JsonProperty("dateOfHiring")
    @NotNull
    private Date dateOfHiring;

}
