package com.inventa.inventa.service;

import com.inventa.inventa.dto.proveedor.ProveedorConContactosRequestDTO;
import com.inventa.inventa.dto.proveedor.ProveedorConContactosUpdateDTO;
import com.inventa.inventa.dto.proveedor.ProveedorSimpleDTO;
import com.inventa.inventa.entity.ContactoProveedor;
import com.inventa.inventa.entity.Proveedor;
import com.inventa.inventa.exceptions.NotFoundException;
import com.inventa.inventa.mapper.ProveedorMapper;
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
    private final ProveedorMapper proveedorMapper;

    public ProveedorService(ProveedorRepository proveedorRepository, ContactoProveedorService contactoProveedorService, ProveedorMapper proveedorMapper) {
        this.proveedorRepository = proveedorRepository;
        this.contactoProveedorService = contactoProveedorService;
        this.proveedorMapper = proveedorMapper;
    }

    public List<Proveedor> listar() {
        return proveedorRepository.findAll();
    }

    public List<ProveedorSimpleDTO> listarSimple() {
        return proveedorRepository.findAll().stream()
                .map(proveedorMapper::toSimpleResponse)
                .collect(Collectors.toList());
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

        // Limpiar la lista existente para sincronizarla con el DTO
        proveedor.getContactos().clear();

        if (!CollectionUtils.isEmpty(dto.getContactos())) {
            dto.getContactos().forEach(contactoDto -> {
                ContactoProveedor contacto = new ContactoProveedor();
                // Si el ID existe, JPA lo tratará como una actualización, si no, como una inserción.
                // Sin embargo, con el clear() de arriba, siempre serán inserciones para el proveedor actual.
                // Para una verdadera actualización de un contacto existente, necesitaríamos un enfoque diferente,
                // pero para el caso de uso de "sincronizar la lista", este es el más limpio.
                
                contacto.setNombreCompleto(contactoDto.getNombreCompleto());
                contacto.setCargo(contactoDto.getCargo());
                contacto.setTelefono(contactoDto.getTelefono());
                contacto.setEmail(contactoDto.getEmail());
                contacto.setProveedor(proveedor);
                proveedor.getContactos().add(contacto);
            });
        }

        return proveedorRepository.save(proveedor);
    }


    public void eliminar(Integer id) {
        proveedorRepository.deleteById(id);
    }

    public Proveedor buscarPorTelefono(String telefono) {
        return proveedorRepository.findByTelefono(telefono);
    }
}
