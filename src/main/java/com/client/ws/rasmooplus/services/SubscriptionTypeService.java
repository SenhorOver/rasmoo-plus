package com.client.ws.rasmooplus.services;

import com.client.ws.rasmooplus.model.SubscriptionType;
import com.client.ws.rasmooplus.repositories.SubscriptionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SubscriptionTypeService {
    @Autowired
    private SubscriptionTypeRepository repository;

    @Transactional(readOnly = true)
    public List<SubscriptionType> findAll() {
        return repository.findAll();
    }
}
