package com.client.ws.rasmooplus.services.impl;

import com.client.ws.rasmooplus.exceptions.NotFoundException;
import com.client.ws.rasmooplus.model.SubscriptionsType;
import com.client.ws.rasmooplus.repositories.SubscriptionTypeRepository;
import com.client.ws.rasmooplus.services.SubscriptionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SubscriptionTypeServiceImpl implements SubscriptionTypeService {
    @Autowired
    private SubscriptionTypeRepository subscriptionTypeRepository;

    @Transactional(readOnly = true)
    @Override
    public List<SubscriptionsType> findAll() {
        return subscriptionTypeRepository.findAll();
    }
    @Transactional(readOnly = true)
    @Override
    public SubscriptionsType findById(Long id) {
        return subscriptionTypeRepository.findById(id).orElseThrow(() -> new NotFoundException("SubscriptionType not found!"));
    }
    @Transactional
    @Override
    public SubscriptionsType create(SubscriptionsType subscriptionType) {
        return null;
    }
    @Transactional
    @Override
    public SubscriptionsType update(Long id, SubscriptionsType subscriptionType) {
        return null;
    }
    @Transactional
    @Override
    public void delete(Long id) {

    }
}
