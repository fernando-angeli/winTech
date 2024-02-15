package com.wintech.wtclientservice.services;

import com.wintech.wtclientservice.domains.Address;
import com.wintech.wtclientservice.domains.Client;
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
import org.springframework.transaction.annotation.Transactional;

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
        if(verifyIsEmpty(String.valueOf(clientDto.getDeliveryAddress()))){
            Address addressDelivery = convertToEntity(addressService.insert(clientDto.getDeliveryAddress()), Address.class);
            client.setDeliveryAddress(addressDelivery);
        } else{
            client.setDeliveryAddress(address);
        }
        repository.save(client);
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
        /*
        * AJUSTAR A REGRA PARA PEGAR O id atual do endereço do cliente e puxar do serviço de endereço
        * com o getReferenceById do endereço atual
        * No caso de incluir um endereço novo de entrega, cadastra o mesmo, se não manter a mesma referencia do
        * endereço principal
        * */
        if(verifyIsEmpty(String.valueOf(clientUpdateDto.getAddress()))) {
            Address updatedAddress = convertToEntity(addressService.update(client.getAddress().getId(), clientUpdateDto.getAddress()), Address.class);
            client.setAddress(updatedAddress);
            if(!verifyIsEmpty(String.valueOf(clientUpdateDto.getDeliveryAddress())))
                client.setDeliveryAddress(updatedAddress);
        }
        if (verifyIsEmpty(String.valueOf(clientUpdateDto.getDeliveryAddress()))) {
            Address updatedDeliveryAddress = convertToEntity(addressService.update(client.getDeliveryAddress().getId(), clientUpdateDto.getAddress()), Address.class);
            client.setAddress(updatedDeliveryAddress);
        }
        client.getType().clear();
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

    private boolean verifyIsEmpty(String jsonString){
        return jsonString.isEmpty();
    }

}
