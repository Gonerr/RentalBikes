package com.bikerental.services;

import com.bikerental.models.*;
import com.bikerental.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    private static final Logger log = LoggerFactory.getLogger(ClientService.class);
    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // Создание клиента
    public Client createClient(String name, String email, String phone) {
        Client client = new Client();
        client.setName(name);
        client.setEmail(email);
        client.setPhone(phone);
        return clientRepository.save(client);
    }

    // Поиск клиента по ID
    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client findByPhone(String phone) {
        return clientRepository.findByPhone(phone);
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }
}