package com.point.sale.service.impl;

import com.point.sale.entity.Producto;
import com.point.sale.entity.ResponseQuery;
import com.point.sale.repository.ProductoDAO;
import com.point.sale.service.ProductoService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoDAO productoDAO;

    public ProductoServiceImpl(ProductoDAO productoDAO) {
        this.productoDAO = productoDAO;
    }

    @Override
    public ResponseQuery saveProducto(Producto producto,boolean nuevo) {
        try{
            producto.setProduct(convertirInicioPalabrasMayuscula(producto.getProduct()));

            if(nuevo && searchProductoByCodeBar(producto.getBar()).getResult()!=null)
            {
                throw new Exception("Codigo de barra ya utilizado");
            }

            productoDAO.save(producto);
            return ResponseQuery.builder().message("ok").status(0).build();
        }catch (Exception e)
        {
            return ResponseQuery.builder().message(e.getMessage()).status(-10).build();
        }
    }

    @Override
    public ResponseQuery searchProductoByName(String name) {
        try{
            List<Producto> productos = productoDAO.getProductsByName(name);
            return ResponseQuery.builder().message("ok").status(0).result(productos).build();
        }catch (Exception e)
        {
            return ResponseQuery.builder().message(e.getMessage()).status(-10).build();
        }
    }

    @Override
    public ResponseQuery searchProductoByCodeBar(String bar) {
        try{
            Producto producto = productoDAO.findByBar(bar);
            return ResponseQuery.builder().message("ok").status(0).result(producto).build();
        }catch (Exception e)
        {
            return ResponseQuery.builder().message("ko").status(-10).build();
        }
    }


    @Override
    public ResponseQuery deleteProducto(Producto producto) {
        try{
            productoDAO.delete(producto);
            return ResponseQuery.builder().message("ok").status(0).build();
        }catch (Exception e)
        {
            return ResponseQuery.builder().message("ko").status(-10).build();
        }
    }
    private String convertirInicioPalabrasMayuscula(String input) {
        StringBuilder resultado = new StringBuilder();
        input = input.trim();

        boolean capitalizarSiguiente = true;


        for (char c : input.toCharArray()) {

            if (Character.isWhitespace(c) || c == '\n' || c == '\r') {
                capitalizarSiguiente = true;
                resultado.append(c);
            } else {

                if (capitalizarSiguiente) {
                    resultado.append(Character.toUpperCase(c));
                    capitalizarSiguiente = false;
                } else {
                    resultado.append(c);
                }
            }
        }

        return resultado.toString();
    }


}
