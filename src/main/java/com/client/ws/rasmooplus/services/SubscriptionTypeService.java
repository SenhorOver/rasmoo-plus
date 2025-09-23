package com.client.ws.rasmooplus.services;


import com.client.ws.rasmooplus.model.SubscriptionsType;

import java.util.List;

public interface SubscriptionTypeService {
    List<SubscriptionsType> findAll();

    SubscriptionsType findById(Long id);

    SubscriptionsType create(SubscriptionsType subscriptionType);

    SubscriptionsType update(Long id, SubscriptionsType subscriptionType);

    void delete(Long id);
}
