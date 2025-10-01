package com.inventa.inventa.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.inventa.inventa.dto.contactoproveedor.ContactoProveedorRequestDTO;
import com.inventa.inventa.dto.contactoproveedor.ContactoProveedorResponseDTO;
import com.inventa.inventa.entity.ContactoProveedor;
import com.inventa.inventa.mapper.ContactoProveedorMapper;
import com.inventa.inventa.service.ContactoProveedorService;

@RestController
@RequestMapping("/api/contactos-proveedor")
public class ContactoProveedorController {

    private final ContactoProveedorService contactoProveedorService;
    private final ContactoProveedorMapper contactoProveedorMapper;

    public ContactoProveedorController(ContactoProveedorService contactoProveedorService,
            ContactoProveedorMapper contactoProveedorMapper) {
        this.contactoProveedorService = contactoProveedorService;
        this.contactoProveedorMapper = contactoProveedorMapper;
    }

    @GetMapping
    public List<ContactoProveedorResponseDTO> listar() {
        return contactoProveedorService.listar().stream().map(contactoProveedorMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ContactoProveedorResponseDTO obtenerPorId(@PathVariable Integer id) {
        ContactoProveedor contacto = contactoProveedorService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contacto no encontrado"));
        return contactoProveedorMapper.toResponse(contacto);
    }

    @GetMapping("/proveedor/{proveedorId}")
    public List<ContactoProveedorResponseDTO> listarPorProveedor(@PathVariable Integer proveedorId) {
        return contactoProveedorService.listarPorProveedor(proveedorId).stream()
                .map(contactoProveedorMapper::toResponse).collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContactoProveedorResponseDTO crear(@RequestBody ContactoProveedorRequestDTO dto) {
        ContactoProveedor contacto = new ContactoProveedor();
        contactoProveedorMapper.updateEntityFromRequest(contacto, dto);
        return contactoProveedorMapper.toResponse(contactoProveedorService.guardar(contacto));
    }

    @PutMapping("/{id}")
    public ContactoProveedorResponseDTO actualizar(@PathVariable Integer id,
            @RequestBody ContactoProveedorRequestDTO dto) {
        ContactoProveedor contacto = contactoProveedorService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contacto no encontrado"));
        contactoProveedorMapper.updateEntityFromRequest(contacto, dto);
        return contactoProveedorMapper.toResponse(contactoProveedorService.guardar(contacto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        if (contactoProveedorService.buscarPorId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contacto no encontrado");
        }
        contactoProveedorService.eliminar(id);
    }
}
