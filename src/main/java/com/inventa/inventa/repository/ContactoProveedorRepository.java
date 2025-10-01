package com.inventa.inventa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.inventa.inventa.entity.ContactoProveedor;

@Repository
public interface ContactoProveedorRepository extends JpaRepository<ContactoProveedor, Integer> {
    List<ContactoProveedor> findByProveedorProveedorId(Integer proveedorId);
}
