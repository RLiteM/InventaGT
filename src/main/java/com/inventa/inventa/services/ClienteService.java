package com.inventa.inventa.services;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.inventa.inventa.entities.Cliente;
import com.inventa.inventa.repositories.ClienteRepository;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Integer id) {
        return clienteRepository.findById(id);
    }

    public Cliente guardar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public void eliminar(Integer id) {
        clienteRepository.deleteById(id);
    }

    public Cliente buscarPorIdentificacionFiscal(String identificacion) {
        return clienteRepository.findByIdentificacionFiscal(identificacion);
    }
}
