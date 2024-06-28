package com.point.sale.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "productos")
@Getter
@Setter
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String product;
    private Date dateLastModification;
    private double price;
    private int stock;
    private String bar;
    private double priceMayorista;
    @Transient
    private double ganancia;

    public double getGanancia() {
        return price-priceMayorista;
    }
}
