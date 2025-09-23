package com.client.ws.rasmooplus.controllers;

import com.client.ws.rasmooplus.exceptions.NotFoundException;
import com.client.ws.rasmooplus.model.SubscriptionsType;
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
    public ResponseEntity<List<SubscriptionsType>> findAll() {
        List<SubscriptionsType> subscriptionTypeList = subscriptionTypeService.findAll();
        return ResponseEntity.ok(subscriptionTypeList);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<SubscriptionsType> findById(@PathVariable("id") Long id) {
        SubscriptionsType subscriptionType = subscriptionTypeService.findById(id);
        return ResponseEntity.ok(subscriptionType);
    }
}
