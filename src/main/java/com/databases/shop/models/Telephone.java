package com.databases.shop.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Telephone {

    @Id
    @NotNull
    @Column(length = 30)
    private String telNumber;

    @NotNull
    @Min(0)
    @Max(2)
    @Column(name = "tel_ord_num")
    private int telOrderNum;

    @NotNull
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Salesman salesman;

    public Telephone(@NotNull String telNumber) {
        this.telNumber = telNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Telephone)) return false;
        Telephone telephone = (Telephone) o;
        return getTelOrderNum() == telephone.getTelOrderNum() &&
                getTelNumber().equals(telephone.getTelNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTelNumber(), getTelOrderNum());
    }
}
