package com.inventa.inventa.service;

import com.inventa.inventa.dto.proveedor.ProveedorConContactosRequestDTO;
import com.inventa.inventa.dto.proveedor.ProveedorConContactosUpdateDTO;
import com.inventa.inventa.entity.ContactoProveedor;
import com.inventa.inventa.entity.Proveedor;
import com.inventa.inventa.exceptions.NotFoundException;
import com.inventa.inventa.repository.ProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Transactional
    public Proveedor actualizarProveedorConContactos(Integer proveedorId, ProveedorConContactosUpdateDTO dto) {
        Proveedor proveedor = proveedorRepository.findById(proveedorId)
                .orElseThrow(() -> new NotFoundException("Proveedor no encontrado con id: " + proveedorId));

        proveedor.setNombreEmpresa(dto.getNombreEmpresa());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setDireccion(dto.getDireccion());

        // Si no se envían contactos en el DTO, no se modifican los existentes.
        if (dto.getContactos() == null) {
            return proveedorRepository.save(proveedor);
        }

        Map<Integer, ContactoProveedor> contactosExistentesMap = proveedor.getContactos().stream()
                .collect(Collectors.toMap(ContactoProveedor::getContactoId, Function.identity()));

        List<ContactoProveedor> contactosActualizados = new ArrayList<>();

        for (var contactoDto : dto.getContactos()) {
            ContactoProveedor contacto;
            if (contactoDto.getId() != null) {
                // Actualización de un contacto existente
                contacto = contactosExistentesMap.remove(contactoDto.getId());
                if (contacto == null) {
                    // Opcional: lanzar excepción si se intenta actualizar un contacto que no existe o no pertenece al proveedor
                    continue; // Ignorar o manejar el error
                }
            } else {
                // Creación de un nuevo contacto
                contacto = new ContactoProveedor();
                contacto.setProveedor(proveedor);
            }
            
            contacto.setNombreCompleto(contactoDto.getNombreCompleto());
            contacto.setCargo(contactoDto.getCargo());
            contacto.setTelefono(contactoDto.getTelefono());
            contacto.setEmail(contactoDto.getEmail());
            contactosActualizados.add(contactoProveedorService.guardar(contacto));
        }

        // Los contactos que quedan en contactosExistentesMap son para eliminar
        contactosExistentesMap.values().forEach(contacto -> contactoProveedorService.eliminar(contacto.getContactoId()));

        proveedor.setContactos(contactosActualizados);
        return proveedorRepository.save(proveedor);
    }


    public void eliminar(Integer id) {
        proveedorRepository.deleteById(id);
    }

    public Proveedor buscarPorTelefono(String telefono) {
        return proveedorRepository.findByTelefono(telefono);
    }
}
