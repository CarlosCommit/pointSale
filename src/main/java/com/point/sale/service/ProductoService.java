package com.point.sale.service;

import com.point.sale.entity.Producto;
import com.point.sale.entity.ResponseQuery;

public interface ProductoService {
    ResponseQuery saveProducto(Producto producto,boolean nuevo);
    ResponseQuery searchProductoByName(String name);
    ResponseQuery searchProductoByCodeBar(String bar);
    ResponseQuery deleteProducto(Producto producto);
}

