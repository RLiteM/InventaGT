package com.inventa.inventa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventa.inventa.entity.ContactoProveedor;

@Repository
public interface ContactoProveedorRepository extends JpaRepository<ContactoProveedor, Integer> {
}
