package com.client.ws.rasmooplus.services;


import com.client.ws.rasmooplus.dto.SubscriptionTypeDto;
import com.client.ws.rasmooplus.model.SubscriptionType;

import java.util.List;

public interface SubscriptionTypeService {
    List<SubscriptionTypeDto> findAll();

    SubscriptionTypeDto findById(Long id);

    SubscriptionTypeDto create(SubscriptionTypeDto dto);

    SubscriptionTypeDto update(Long id, SubscriptionTypeDto dto);

    void delete(Long id);
}
