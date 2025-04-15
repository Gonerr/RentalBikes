package com.bikerental.services;

import com.bikerental.models.*;
import com.bikerental.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    // Создание клиента
    public Client createClient(String name, String contactInfo) {
        Client client = new Client();
        client.setName(name);                   // Добавление имени и контактной информации
        client.setContactInfo(contactInfo);
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
}