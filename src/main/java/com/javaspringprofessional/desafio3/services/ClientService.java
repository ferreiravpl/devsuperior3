package com.javaspringprofessional.desafio3.services;

import com.javaspringprofessional.desafio3.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

}
