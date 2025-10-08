package com.inventa.inventa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventa.inventa.entity.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    Cliente findByIdentificacionFiscal(String identificacionFiscal);

    long countByTipoCliente(Cliente.TipoCliente tipoCliente);
    
}