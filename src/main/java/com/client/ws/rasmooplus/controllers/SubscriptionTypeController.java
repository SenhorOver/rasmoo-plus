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
    private SubscriptionTypeServiceImpl subscriptionTypeService;

    @GetMapping
    public ResponseEntity<List<SubscriptionTypeDto>> findAll() {
        List<SubscriptionTypeDto> subscriptionTypeDtoList = subscriptionTypeService.findAll();
        return ResponseEntity.ok(subscriptionTypeDtoList);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<SubscriptionTypeDto> findById(@PathVariable("id") Long id) {
        SubscriptionTypeDto subscriptionTypeDto = subscriptionTypeService.findById(id);
        return ResponseEntity.ok(subscriptionTypeDto);
    }

    @PostMapping
    public ResponseEntity<SubscriptionTypeDto> create(@RequestBody SubscriptionTypeDto subscriptionType) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionTypeService.create(subscriptionType));
    }
}
