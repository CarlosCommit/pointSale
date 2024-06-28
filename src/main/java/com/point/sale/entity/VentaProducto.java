package com.point.sale.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "ventas_productos")
@Getter
@Setter

public class VentaProducto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int cantidad;
    private double priceUnitary;
    private double priceTotal;
    @ManyToOne
    private Producto producto;
    @ManyToOne(cascade= CascadeType.PERSIST)
    private Venta venta;

}
