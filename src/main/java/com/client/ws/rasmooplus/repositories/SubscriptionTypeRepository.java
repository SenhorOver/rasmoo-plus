package com.client.ws.rasmooplus.repositories;

import com.client.ws.rasmooplus.model.SubscriptionsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionTypeRepository extends JpaRepository<SubscriptionsType, Long> {
}
