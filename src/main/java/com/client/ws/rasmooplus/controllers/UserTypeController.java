package com.client.ws.rasmooplus.controllers;

import com.client.ws.rasmooplus.dto.UserTypeDto;
import com.client.ws.rasmooplus.services.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/user-type")
public class UserTypeController {

    @Autowired
    private UserTypeService service;

    @GetMapping
    public List<UserTypeDto> findAll() {
        return service.findAll();
    }

}
