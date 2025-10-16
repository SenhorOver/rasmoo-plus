package com.client.ws.rasmooplus.repositories.jpa;

import com.client.ws.rasmooplus.model.jpa.SubscriptionType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@WebMvcTest(SubscriptionTypeRepository.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(profiles = "test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SubscriptionTypeRepositoryTest {

    @Autowired
    private SubscriptionTypeRepository subscriptionTypeRepository;

    @BeforeAll
    void loadSubscriptions() {
        List<SubscriptionType> subscriptionTypeList = new ArrayList<>();
        SubscriptionType subscriptionTypeDto1 = new SubscriptionType(null, "VITALICIO", null, BigDecimal.valueOf(997), "FOREVER2025");
        SubscriptionType subscriptionTypeDto2 = new SubscriptionType(null, "ANUAL", 12, BigDecimal.valueOf(297), "YEARLY025");
        SubscriptionType subscriptionTypeDto3 = new SubscriptionType(null, "MENSAL", 1, BigDecimal.valueOf(35), "MONTHLY2025");
        subscriptionTypeList.add(subscriptionTypeDto1);
        subscriptionTypeList.add(subscriptionTypeDto2);
        subscriptionTypeList.add(subscriptionTypeDto3);

        subscriptionTypeRepository.saveAll(subscriptionTypeList);
    }

    // Não é mais necessário
    @AfterAll
    void dropDatabase() {
        subscriptionTypeRepository.deleteAll();
    }

    @Test
    void given_findByProductKey_when_getProductKey_then_returnCorrectSubscriptionType() {
        assertEquals("VITALICIO", subscriptionTypeRepository.findByProductKey("FOREVER2025").get().getName());
        assertEquals("ANUAL", subscriptionTypeRepository.findByProductKey("YEARLY025").get().getName());
        assertEquals("MENSAL", subscriptionTypeRepository.findByProductKey("MONTHLY2025").get().getName());
    }
}