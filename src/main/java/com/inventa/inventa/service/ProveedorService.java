package com.inventa.inventa.service;

import com.inventa.inventa.dto.proveedor.ProveedorConContactosRequestDTO;
import com.inventa.inventa.entity.ContactoProveedor;
import com.inventa.inventa.entity.Proveedor;
import com.inventa.inventa.repository.ProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final ContactoProveedorService contactoProveedorService;

    public ProveedorService(ProveedorRepository proveedorRepository, ContactoProveedorService contactoProveedorService) {
        this.proveedorRepository = proveedorRepository;
        this.contactoProveedorService = contactoProveedorService;
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

    @Transactional
    public Proveedor crearProveedorConContactos(ProveedorConContactosRequestDTO dto) {
        Proveedor proveedor = new Proveedor();
        proveedor.setNombreEmpresa(dto.getNombreEmpresa());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setDireccion(dto.getDireccion());

        Proveedor proveedorGuardado = proveedorRepository.save(proveedor);

        if (!CollectionUtils.isEmpty(dto.getContactos())) {
            List<ContactoProveedor> contactosGuardados = new ArrayList<>();
            dto.getContactos().forEach(contactoDto -> {
                ContactoProveedor nuevoContacto = new ContactoProveedor();
                nuevoContacto.setNombreCompleto(contactoDto.getNombreCompleto());
                nuevoContacto.setCargo(contactoDto.getCargo());
                nuevoContacto.setTelefono(contactoDto.getTelefono());
                nuevoContacto.setEmail(contactoDto.getEmail());
                nuevoContacto.setProveedor(proveedorGuardado);
                contactosGuardados.add(contactoProveedorService.guardar(nuevoContacto));
            });
            proveedorGuardado.setContactos(contactosGuardados);
        }

        return proveedorGuardado;
    }

    public void eliminar(Integer id) {
        proveedorRepository.deleteById(id);
    }

    public Proveedor buscarPorTelefono(String telefono) {
        return proveedorRepository.findByTelefono(telefono);
    }
}
