package com.client.ws.rasmooplus.services.impl;

import com.client.ws.rasmooplus.dto.SubscriptionTypeDto;
import com.client.ws.rasmooplus.exceptions.BadRequestException;
import com.client.ws.rasmooplus.exceptions.NotFoundException;
import com.client.ws.rasmooplus.model.SubscriptionType;
import com.client.ws.rasmooplus.repositories.SubscriptionTypeRepository;
import com.client.ws.rasmooplus.services.SubscriptionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class SubscriptionTypeServiceImpl implements SubscriptionTypeService {
    @Autowired
    private SubscriptionTypeRepository repository;

    @Transactional(readOnly = true)
    @Override
    public List<SubscriptionTypeDto> findAll() {
        List<SubscriptionType> entities = repository.findAll();

        return entities.stream().map(SubscriptionTypeDto::new).toList();
    }
    @Transactional(readOnly = true)
    @Override
    public SubscriptionTypeDto findById(Long id) {
        SubscriptionType entity = getSubscriptionType(id);
        return new SubscriptionTypeDto(entity);
    }

    @Transactional
    @Override
    public SubscriptionTypeDto create(SubscriptionTypeDto dto) {
        if(Objects.nonNull(dto.getId())) {
            throw new BadRequestException("Id deve ser nulo");
        }
         SubscriptionType entity = repository.save(SubscriptionType.builder()
                .id(dto.getId())
                .name(dto.getName())
                .accessMonths(dto.getAccessMonths())
                .price(dto.getPrice())
                .productKey(dto.getProductKey()).build());
        return  new SubscriptionTypeDto(entity);
    }
    @Transactional
    @Override
    public SubscriptionTypeDto update(Long id, SubscriptionTypeDto dto) {
        /*
             Fazer atualização com referência
        SubscriptionType entity = repository.getReferenceById(id);
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setAccessMonths(dto.getAccessMonths());
        entity.setProductKey(dto.getProductKey());
        repository.save(entity);
        */
        getSubscriptionType(id);
        SubscriptionType entity = repository.save(SubscriptionType.builder()
                .id(id)
                .name(dto.getName())
                .accessMonths(dto.getAccessMonths())
                .price(dto.getPrice())
                .productKey(dto.getProductKey()).build());
        return new SubscriptionTypeDto(entity);
    }
    @Transactional
    @Override
    public void delete(Long id) {
        getSubscriptionType(id);
        repository.deleteById(id);
    }

    private SubscriptionType getSubscriptionType(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("SubscriptionType not found!"));
    }
}
