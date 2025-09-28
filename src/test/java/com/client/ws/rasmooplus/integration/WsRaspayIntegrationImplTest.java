package com.client.ws.rasmooplus.integration;

import com.client.ws.rasmooplus.dto.wsraspay.CreditCardDto;
import com.client.ws.rasmooplus.dto.wsraspay.CustomerDto;
import com.client.ws.rasmooplus.dto.wsraspay.OrderDto;
import com.client.ws.rasmooplus.dto.wsraspay.PaymentDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
class WsRaspayIntegrationImplTest {

    @Autowired
    private WsRaspayIntegration wsRaspayIntegration;

    @Test
    void createCustomerWhenDtoOK() {
        CustomerDto dto = new CustomerDto(null, "08605294019", "teste@teste.com", "Marcos", "Silva");
        wsRaspayIntegration.createCustomer(dto);
    }

    @Test
    void createOrderWhenDtoOK() {
        OrderDto dto = new OrderDto(null, "68d8801530857c6b874cc1ef", BigDecimal.ZERO, "MONTH22");
        wsRaspayIntegration.createOrder(dto);
    }

    @Test
    void createPaymentWhenDtoOK() {
        CreditCardDto creditCardDto = new CreditCardDto(123L, "08605294019", 0L, 6L, "1234123412341234", 2025L);
        PaymentDto dto = new PaymentDto(creditCardDto,"68d8801530857c6b874cc1ef","68d880ce30857c6b874cc1f3");
        wsRaspayIntegration.processPayment(dto);
    }

}
