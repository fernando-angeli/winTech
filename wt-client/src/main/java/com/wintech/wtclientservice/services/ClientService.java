package com.wintech.wtclientservice.services;

import com.wintech.wtclientservice.domains.Address;
import com.wintech.wtclientservice.domains.Client;
import com.wintech.wtclientservice.dtos.AddressDto;
import com.wintech.wtclientservice.dtos.ClientDto;
import com.wintech.wtclientservice.dtos.ClientUpdateDto;
import com.wintech.wtclientservice.repositories.ClientRepository;
import com.wintech.wtclientservice.services.exceptions.DatabaseException;
import com.wintech.wtclientservice.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

import static com.wintech.wtclientservice.mapper.EntityMapper.convertToDto;
import static com.wintech.wtclientservice.mapper.EntityMapper.convertToEntity;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    @Autowired
    private AddressService addressService;

    @Transactional
    public ClientDto insert(ClientDto clientDto){
        Address address = convertToEntity(addressService.insert(clientDto.getAddress()), Address.class);
        Client client = convertToEntity(clientDto, Client.class);
        client.setAddress(address);
        if(clientDto.getDeliveryAddress() != null){
            Address addressDelivery = convertToEntity(addressService.insert(clientDto.getDeliveryAddress()), Address.class);
            client.setDeliveryAddress(addressDelivery);
        }
        client = repository.save(client);
        return convertToDto(client, ClientDto.class);
    }

    @Transactional(readOnly = true)
    public Optional<ClientDto> findById(Long id){
        Optional<Client> client = repository.findById(id);
        ClientDto clientDto = convertToDto(client.orElseThrow(() -> new ResourceNotFoundException("Id ["+id+"] não localizado.")), ClientDto.class);
        return Optional.of(clientDto);
    }

    @Transactional(readOnly = true)
    public Page<ClientDto> findAll(Pageable pageable){
        Page<Client> clients = repository.findAll(pageable);
        return clients.map(client -> convertToDto(client, ClientDto.class));
    }

    @Transactional
    public ClientDto update(Long id, ClientUpdateDto clientUpdateDto) {
        verifyExistsId(id);
        Client client = repository.getReferenceById(id);
        client.getType().clear();
        AddressDto updatedAddress = addressService.update(client.getAddress().getId(), clientUpdateDto.getAddress());
        client.setAddress(convertToEntity(updatedAddress, Address.class));
        if (clientUpdateDto.getDeliveryAddress() != null) {
            if(client.getDeliveryAddress() == null){
                AddressDto newDeliveryAddress = convertToDto(clientUpdateDto.getDeliveryAddress(), AddressDto.class);
                client.setDeliveryAddress(convertToEntity(addressService.insert(newDeliveryAddress), Address.class));
            }else{
                AddressDto updateDeliveryAddress = addressService.update(client.getDeliveryAddress().getId(), clientUpdateDto.getDeliveryAddress());
                client.setDeliveryAddress(convertToEntity(updateDeliveryAddress, Address.class));
            }
        } else {
            client.setDeliveryAddress(null);
        }
        convertToEntity(clientUpdateDto, client);
        client = repository.save(client);
        return convertToDto(client, ClientDto.class);
    }

    @Transactional
    public void delete(Long id) {
        try{
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Id [" + id + "] não localizado.");
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException("Não foi possível excluir este registro por violação de integridade do Banco de Dados.");
        }
    }

    private void verifyExistsId(Long id){
        if(!repository.existsById(id)){
            throw new ResourceNotFoundException("Id [" + id + "] não localizado.");
        }
    }

}
