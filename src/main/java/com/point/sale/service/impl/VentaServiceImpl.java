package com.point.sale.service.impl;

import com.point.sale.entity.Producto;
import com.point.sale.entity.ResponseQuery;
import com.point.sale.entity.Venta;
import com.point.sale.entity.VentaProducto;
import com.point.sale.model.ProductView;
import com.point.sale.repository.VentaRepository;
import com.point.sale.service.VentaService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;

    public VentaServiceImpl(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    public ResponseQuery registrarVenta(List<ProductView> productos) {

        try {
            Venta venta = new Venta();
            venta.setDate(new Date());
            List<VentaProducto> ventaProductos = new ArrayList<>();
            for (ProductView producto : productos) {
                VentaProducto ventaProducto = new VentaProducto();
                ventaProducto.setVenta(venta);
                ventaProducto.setCantidad(producto.getCantidad());
                ventaProducto.setPriceTotal(producto.getPreciofinal());
                ventaProducto.setPriceUnitary(producto.getPrecioUnitario());
                Producto prod = new Producto();
                prod.setId(producto.getId());
                ventaProducto.setProducto(prod);
                ventaProductos.add(ventaProducto);
            }
            venta.setSale(ventaProductos);
            venta = ventaRepository.save(venta);
            return ResponseQuery.builder().message("ok").status(0).build();
        } catch (Exception e) {
            return ResponseQuery.builder().message(e.getMessage()).status(-10).build();
        }


    }
}
