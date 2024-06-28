package com.point.sale.model;

import com.point.sale.entity.Producto;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

/**
 * Clase destinada a funcionar como un DTO simplemente para representar el modelo en la tablaView
 */
@Getter
@Setter
public class ProductView {
    private long id;
    private String producto;
    private int cantidad;
    private double precioUnitario;
    private double preciofinal;
    private Date fechaUltModif;



    public ProductView(String producto, int cantidad, double precioUnitario, double preciofinal) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.preciofinal = preciofinal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o instanceof Producto && ((Producto) o).getId() == this.id) return true;
        if (!(o instanceof ProductView that)) return false;
        return cantidad == that.cantidad && Double.compare(precioUnitario, that.precioUnitario) == 0 && Double.compare(preciofinal, that.preciofinal) == 0 && Objects.equals(producto, that.producto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(producto, cantidad, precioUnitario, preciofinal);
    }
}
