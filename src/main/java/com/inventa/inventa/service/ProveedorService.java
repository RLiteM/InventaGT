package com.inventa.inventa.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.inventa.inventa.entity.Proveedor;
import com.inventa.inventa.repository.ProveedorRepository;

@Service
public class ProveedorService {
    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    public List<Proveedor> listar() {
        return proveedorRepository.findAll();
    }

    public Optional<Proveedor> buscarPorId(Integer id) {
        return proveedorRepository.findById(id);
    }

    public Proveedor guardar(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    public void eliminar(Integer id) {
        proveedorRepository.deleteById(id);
    }

    public Proveedor buscarPorTelefono(String telefono) {
        return proveedorRepository.findByTelefono(telefono);
    }
}
