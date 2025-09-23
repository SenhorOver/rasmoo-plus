package com.client.ws.rasmooplus.services.impl;

import com.client.ws.rasmooplus.dto.SubscriptionTypeDto;
import com.client.ws.rasmooplus.exceptions.NotFoundException;
import com.client.ws.rasmooplus.model.SubscriptionType;
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
    public List<SubscriptionTypeDto> findAll() {
        List<SubscriptionType> subscriptionTypeList = subscriptionTypeRepository.findAll();

        return subscriptionTypeList.stream().map(SubscriptionTypeDto::new).toList();
    }
    @Transactional(readOnly = true)
    @Override
    public SubscriptionTypeDto findById(Long id) {
        SubscriptionType subscriptionType = subscriptionTypeRepository.findById(id).orElseThrow(() -> new NotFoundException("SubscriptionType not found!"));
        return new SubscriptionTypeDto(subscriptionType);
    }
    @Transactional
    @Override
    public SubscriptionTypeDto create(SubscriptionTypeDto dto) {
         SubscriptionType subscriptionType = subscriptionTypeRepository.save(SubscriptionType.builder()
                .id(dto.getId())
                .name(dto.getName())
                .accessMonths(dto.getAccessMonths())
                .price(dto.getPrice())
                .productKey(dto.getProductKey()).build());
        return  new SubscriptionTypeDto(subscriptionType);
    }
    @Transactional
    @Override
    public SubscriptionTypeDto update(Long id, SubscriptionTypeDto dto) {
        return null;
    }
    @Transactional
    @Override
    public void delete(Long id) {

    }
}
