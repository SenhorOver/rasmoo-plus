package com.client.ws.rasmooplus.controllers;

import com.client.ws.rasmooplus.dto.SubscriptionTypeDto;
import com.client.ws.rasmooplus.model.SubscriptionType;
import com.client.ws.rasmooplus.services.impl.SubscriptionTypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/subscription-type")
public class SubscriptionTypeController {

    @Autowired
    private SubscriptionTypeServiceImpl service;

    @GetMapping
    public ResponseEntity<List<SubscriptionTypeDto>> findAll() {
        List<SubscriptionTypeDto> dtoList = service.findAll();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<SubscriptionTypeDto> findById(@PathVariable("id") Long id) {
        SubscriptionTypeDto dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<SubscriptionTypeDto> create(@RequestBody SubscriptionTypeDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<SubscriptionTypeDto> update(
            @PathVariable("id") Long id,
            @RequestBody SubscriptionTypeDto dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
