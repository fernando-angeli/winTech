package com.wintech.wtclientservice.controllers;

import com.wintech.wtclientservice.dtos.ClientDto;
import com.wintech.wtclientservice.dtos.ClientUpdateDto;
import com.wintech.wtclientservice.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(value = "/clients")
public class ClientController {

    @Autowired
    private ClientService service;

    @PostMapping
    public ResponseEntity<ClientDto> insert(@RequestBody ClientDto clientDto){
        ClientDto newClient = service.insert(clientDto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newClient.getId())
                .toUri();
        return ResponseEntity.created(uri).body(newClient);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ClientDto>> findById(@PathVariable Long id){
        Optional<ClientDto> clientDto = service.findById(id);
        return ResponseEntity.ok().body(clientDto);
    }

    @GetMapping
    public ResponseEntity<Page<ClientDto>> findAll(Pageable pageable){
        Page<ClientDto> clients = service.findAll(pageable);
        return ResponseEntity.ok().body(clients);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDto> update(@PathVariable Long id, @Valid @RequestBody ClientUpdateDto clientUpdateDto){
        ClientDto newClient = service.update(id, clientUpdateDto);
        return ResponseEntity.ok().body(newClient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
