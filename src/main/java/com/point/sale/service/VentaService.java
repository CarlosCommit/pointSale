package com.point.sale.service;

import com.point.sale.entity.ResponseQuery;
import com.point.sale.model.ProductView;

import java.util.List;

public interface VentaService {
    ResponseQuery registrarVenta(List<ProductView> productos);
}
