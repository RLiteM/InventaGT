package com.inventa.inventa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.inventa.inventa.entity.ContactoProveedor;
import com.inventa.inventa.repository.ContactoProveedorRepository;

@Service
public class ContactoProveedorService {

    private final ContactoProveedorRepository contactoProveedorRepository;

    public ContactoProveedorService(ContactoProveedorRepository contactoProveedorRepository) {
        this.contactoProveedorRepository = contactoProveedorRepository;
    }

    public List<ContactoProveedor> listar() {
        return contactoProveedorRepository.findAll();
    }

    public Optional<ContactoProveedor> buscarPorId(Integer id) {
        return contactoProveedorRepository.findById(id);
    }

    public List<ContactoProveedor> listarPorProveedor(Integer proveedorId) {
        return contactoProveedorRepository.findByProveedorProveedorId(proveedorId);
    }

    public ContactoProveedor guardar(ContactoProveedor contactoProveedor) {
        return contactoProveedorRepository.save(contactoProveedor);
    }

    public void eliminar(Integer id) {
        contactoProveedorRepository.deleteById(id);
    }
}
