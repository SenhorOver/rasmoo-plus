package com.client.ws.rasmooplus.controllers;

import com.client.ws.rasmooplus.model.SubscriptionType;
import com.client.ws.rasmooplus.services.SubscriptionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/subscription-type")
public class SubscriptionTypeController {

    @Autowired
    private SubscriptionTypeService service;

    @GetMapping
    public ResponseEntity<List<SubscriptionType>> findAll() {
        List<SubscriptionType> subscriptionTypeList = service.findAll();
        return ResponseEntity.ok(subscriptionTypeList);
    }
}
