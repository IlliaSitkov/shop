package com.databases.shop.mapstruct.dtos.salesman;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
public class TelephoneDto {

    @JsonProperty("tel_num")
    @NotNull
    private String telNumber;

    @JsonProperty("tel_ord_num")
    @NotNull
    private int telOrderNum;

    public TelephoneDto(@NotNull String telNumber, @NotNull int telOrderNum) {
        this.telNumber = telNumber;
        this.telOrderNum = telOrderNum;
    }
}
