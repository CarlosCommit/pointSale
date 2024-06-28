package com.point.sale.repository;

import com.point.sale.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoDAO extends JpaRepository<Producto,Long> {

    @Query(value = "SELECT * FROM pos.productos WHERE nombre LIKE %:name%", nativeQuery = true)
    List<Producto> getProductsByName(@Param("name")String name);
    Producto findByBar(String bar);
}
