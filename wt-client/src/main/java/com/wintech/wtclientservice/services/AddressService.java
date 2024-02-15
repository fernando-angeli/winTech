package com.wintech.wtclientservice.services;

import com.wintech.wtclientservice.domains.Address;
import com.wintech.wtclientservice.domains.Client;
import com.wintech.wtclientservice.dtos.AddressDto;
import com.wintech.wtclientservice.dtos.AddressUpdateDto;
import com.wintech.wtclientservice.dtos.ClientDto;
import com.wintech.wtclientservice.repositories.AddressRepository;
import com.wintech.wtclientservice.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.wintech.wtclientservice.mapper.EntityMapper.convertToDto;
import static com.wintech.wtclientservice.mapper.EntityMapper.convertToEntity;

@Service
public class AddressService {

    @Autowired
    private AddressRepository repository;

    @Transactional
    public AddressDto insert(AddressDto addressDto){
        Address address = convertToEntity(addressDto, Address.class);
        address = repository.save(address);
        return convertToDto(address, AddressDto.class);
    }

    @Transactional
    public AddressDto update(Long id, AddressUpdateDto addressUpdateDto) {
        Address address = repository.getReferenceById(id);
        convertToEntity(addressUpdateDto, address);
        address = repository.save(address);
        return convertToDto(address, AddressDto.class);
    }

    private void verifyExistsId(Long id){
        if(!repository.existsById(id)){
            throw new ResourceNotFoundException("Id [" + id + "] não localizado.");
        }
    }

}
