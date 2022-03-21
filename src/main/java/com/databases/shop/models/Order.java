package com.databases.shop.models;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Table(name = "order_t")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Salesman salesman;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "order")
    private Set<ProductInOrder> products = new HashSet<>();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created", nullable = false)
    private Date date;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Min(0)
    @Column(name = "order_cost")
    private double cost;

    @OneToMany(mappedBy = "order")
    private Set<ProductInOrder> productsInOrder = new HashSet<>();
}
