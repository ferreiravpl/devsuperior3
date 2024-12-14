package com.javaspringprofessional.desafio3.services;

import com.javaspringprofessional.desafio3.dto.ClientDTO;
import com.javaspringprofessional.desafio3.entities.Client;
import com.javaspringprofessional.desafio3.repositories.ClientRepository;
import com.javaspringprofessional.desafio3.services.exceptions.DatabaseException;
import com.javaspringprofessional.desafio3.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.javaspringprofessional.desafio3.util.Constants.NOT_FOUND_RESOURCE;
import static com.javaspringprofessional.desafio3.util.Constants.REFERENCIAL_INTEGRITY_FAILURE;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public ClientDTO findById(Long id) {
        return new ClientDTO(clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_RESOURCE)));
    }

    @Transactional(readOnly = true)
    public Page<ClientDTO> findAll(Pageable pageable) {
    return clientRepository.findAll(pageable)
            .map(ClientDTO::new);
    }

    @Transactional
    public ClientDTO insert(ClientDTO clientDTO) {
        Client clientEntity = translateEntity(clientDTO, new Client());
        clientRepository.save(clientEntity);
        return new ClientDTO(clientEntity);
    }

    @Transactional
    public ClientDTO update(Long id, ClientDTO clientDTO) {
        try {
            Client clientEntity = translateEntity(clientDTO, clientRepository.getReferenceById(id));
            clientRepository.save(clientEntity);
            return new ClientDTO(clientEntity);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(NOT_FOUND_RESOURCE);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        validateClient(id);
        deleteClient(id);
    }

    private Client translateEntity(ClientDTO clientDTO, Client clientEntity) {
        clientEntity.setName(clientDTO.getName());
        clientEntity.setCpf(clientDTO.getCpf());
        clientEntity.setIncome(clientDTO.getIncome());
        clientEntity.setChildren(clientDTO.getChildren());
        clientEntity.setBirthDate(clientDTO.getBirthDate());
        return clientEntity;
    }

    private void validateClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException(NOT_FOUND_RESOURCE);
        }
    }

    private void deleteClient(Long id) {
        try {
            clientRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(REFERENCIAL_INTEGRITY_FAILURE);
        }
    }
}
