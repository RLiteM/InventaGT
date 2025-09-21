package com.inventa.inventa.repositories;

import com.inventa.inventa.entities.ContactoProveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactoProveedorRepository extends JpaRepository<ContactoProveedor, Integer> {
}
