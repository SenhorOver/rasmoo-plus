package com.client.ws.rasmooplus.services.impl;

import com.client.ws.rasmooplus.controllers.SubscriptionTypeController;
import com.client.ws.rasmooplus.dto.SubscriptionTypeDto;
import com.client.ws.rasmooplus.exceptions.BadRequestException;
import com.client.ws.rasmooplus.exceptions.NotFoundException;
import com.client.ws.rasmooplus.mapper.SubscriptionTypeMapper;
import com.client.ws.rasmooplus.model.jpa.SubscriptionType;
import com.client.ws.rasmooplus.repositories.SubscriptionTypeRepository;
import com.client.ws.rasmooplus.services.SubscriptionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class SubscriptionTypeServiceImpl implements SubscriptionTypeService {
    private static final String UPDATE = "update";
    private static final String DELETE = "delete";

    @Autowired
    private SubscriptionTypeRepository repository;

    @Transactional(readOnly = true)
    @Override
    @Cacheable(value = "subscriptionType")
    public List<SubscriptionTypeDto> findAll() {
        List<SubscriptionType> entities = repository.findAll();

        return entities.stream().map(SubscriptionTypeMapper::fromEntityToDto).toList();
    }
    @Transactional(readOnly = true)
    @Override
    @Cacheable(value = "subscriptionType", key = "#id")
    public SubscriptionTypeDto findById(Long id) {
        return SubscriptionTypeMapper.fromEntityToDto(getSubscriptionType(id)).add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(SubscriptionTypeController.class).findById(id))
                .withSelfRel()
        ).add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(SubscriptionTypeController.class).update(id, new SubscriptionTypeDto()))
                .withRel(UPDATE)
        ).add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(SubscriptionTypeController.class).delete(id))
                .withRel(DELETE)
        );
    }

    @Transactional
    @Override
    @CacheEvict(value = "subscriptionType", allEntries = true)
    public SubscriptionTypeDto create(SubscriptionTypeDto dto) {
        if(Objects.nonNull(dto.getId())) {
            throw new BadRequestException("Id deve ser nulo");
        }
        return  SubscriptionTypeMapper.fromEntityToDto(repository.save(SubscriptionTypeMapper.fromDtoToEntity(dto)));
    }
    @Transactional
    @Override
    @CacheEvict(value = "subscriptionType", allEntries = true)
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
        dto.setId(id);
        return SubscriptionTypeMapper.fromEntityToDto(repository.save(SubscriptionTypeMapper.fromDtoToEntity(dto)));
    }
    @Transactional
    @Override
    @CacheEvict(value = "subscriptionType", allEntries = true)
    public void delete(Long id) {
        getSubscriptionType(id);
        repository.deleteById(id);
    }

    private SubscriptionType getSubscriptionType(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("SubscriptionType not found!"));
    }
}
