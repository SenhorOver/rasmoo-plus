package com.client.ws.rasmooplus.integration;

import com.client.ws.rasmooplus.dto.wsraspay.CustomerDto;
import com.client.ws.rasmooplus.dto.wsraspay.OrderDto;
import com.client.ws.rasmooplus.dto.wsraspay.PaymentDto;
import com.client.ws.rasmooplus.exceptions.HttpClientException;
import com.client.ws.rasmooplus.integration.impl.WsRaspayIntegrationImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WsRaspayIntegrationTest {

    @Mock
    public RestTemplate restTemplate;

    @InjectMocks
    private WsRaspayIntegrationImpl wsRaspayIntegration;

    private static HttpHeaders headers;

    @BeforeAll
    static void loadHeaders() {
        headers = getHttpHeaders();
    }

    @Test
    void given_createCustomer_when_apiResponseIs201Created_then_returnCustomerDto() {
        ReflectionTestUtils.setField(wsRaspayIntegration, "raspayHost", "http://localhost:8080");
        ReflectionTestUtils.setField(wsRaspayIntegration, "customerUrl", "/customer");
        CustomerDto dto = new CustomerDto();
        dto.setCpf("11122233345");
        HttpEntity<CustomerDto> request = new HttpEntity<>(dto, headers);
        when(restTemplate.exchange(
                "http://localhost:8080/customer", HttpMethod.POST, request, CustomerDto.class))
                .thenReturn(ResponseEntity.of(Optional.of(dto)));
        assertEquals(dto, wsRaspayIntegration.createCustomer(dto));
        verify(restTemplate,times(1)).exchange(
                "http://localhost:8080/customer", HttpMethod.POST, request, CustomerDto.class);

    }

    @Test
    void given_createCustomer_when_apiResponseIs400BadRequest_then_returnNull() {
        ReflectionTestUtils.setField(wsRaspayIntegration, "raspayHost", "http://localhost:8080");
        ReflectionTestUtils.setField(wsRaspayIntegration, "customerUrl", "/customer");
        CustomerDto dto = new CustomerDto();
        dto.setCpf("11122233345");
        HttpEntity<CustomerDto> request = new HttpEntity<>(dto, headers);
        when(restTemplate.exchange(
                "http://localhost:8080/customer", HttpMethod.POST, request, CustomerDto.class))
                .thenReturn(ResponseEntity.badRequest().build());
        assertNull(wsRaspayIntegration.createCustomer(dto));
        verify(restTemplate,times(1)).exchange(
                "http://localhost:8080/customer", HttpMethod.POST, request, CustomerDto.class);

    }

    @Test
    void given_createCustomer_when_apiResponseGetThrows_then_throwsHttpClientException() {
        ReflectionTestUtils.setField(wsRaspayIntegration, "raspayHost", "http://localhost:8080");
        ReflectionTestUtils.setField(wsRaspayIntegration, "customerUrl", "/customer");
        CustomerDto dto = new CustomerDto();
        dto.setCpf("11122233345");
        HttpEntity<CustomerDto> request = new HttpEntity<>(dto, headers);
        when(restTemplate.exchange(
                "http://localhost:8080/customer", HttpMethod.POST, request, CustomerDto.class))
                .thenThrow(HttpClientException.class);
        assertThrows(HttpClientException.class,() -> wsRaspayIntegration.createCustomer(dto));
        verify(restTemplate,times(1)).exchange(
                "http://localhost:8080/customer", HttpMethod.POST, request, CustomerDto.class);

    }

    @Test
    void given_createOrder_when_apiResponseIs201Created_then_returnOrderDto() {
        ReflectionTestUtils.setField(wsRaspayIntegration, "raspayHost", "http://localhost:8080");
        ReflectionTestUtils.setField(wsRaspayIntegration, "orderUrl", "/order");
        OrderDto dto = new OrderDto();
        dto.setCustomerId("123");
        HttpEntity<OrderDto> request = new HttpEntity<>(dto, headers);
        when(restTemplate.exchange(
                "http://localhost:8080/order", HttpMethod.POST, request, OrderDto.class))
                .thenReturn(ResponseEntity.of(Optional.of(dto)));
        assertEquals(dto, wsRaspayIntegration.createOrder(dto));
        verify(restTemplate,times(1)).exchange(
                "http://localhost:8080/order", HttpMethod.POST, request, OrderDto.class);

    }

    @Test
    void given_createOrder_when_apiResponseIs400BadRequest_then_returnNull() {
        ReflectionTestUtils.setField(wsRaspayIntegration, "raspayHost", "http://localhost:8080");
        ReflectionTestUtils.setField(wsRaspayIntegration, "orderUrl", "/order");
        OrderDto dto = new OrderDto();
        dto.setCustomerId("123");
        HttpEntity<OrderDto> request = new HttpEntity<>(dto, headers);
        when(restTemplate.exchange(
                "http://localhost:8080/order", HttpMethod.POST, request, OrderDto.class))
                .thenReturn(ResponseEntity.badRequest().build());
        assertNull(wsRaspayIntegration.createOrder(dto));
        verify(restTemplate,times(1)).exchange(
                "http://localhost:8080/order", HttpMethod.POST, request, OrderDto.class);

    }

    @Test
    void given_createOrder_when_apiResponseGetThrows_then_ThrowHttpClientException() {
        ReflectionTestUtils.setField(wsRaspayIntegration, "raspayHost", "http://localhost:8080");
        ReflectionTestUtils.setField(wsRaspayIntegration, "orderUrl", "/order");
        OrderDto dto = new OrderDto();
        dto.setCustomerId("123");
        HttpEntity<OrderDto> request = new HttpEntity<>(dto, headers);
        when(restTemplate.exchange(
                "http://localhost:8080/order", HttpMethod.POST, request, OrderDto.class))
                .thenThrow(HttpClientException.class);
        assertThrows(HttpClientException.class, () -> wsRaspayIntegration.createOrder(dto));
        verify(restTemplate,times(1)).exchange(
                "http://localhost:8080/order", HttpMethod.POST, request, OrderDto.class);

    }

    @Test
    void given_processPayment_when_apiResponseIs200Success_then_returnTrue() {
        ReflectionTestUtils.setField(wsRaspayIntegration, "raspayHost", "http://localhost:8080");
        ReflectionTestUtils.setField(wsRaspayIntegration, "paymentUrl", "/payment");
        PaymentDto dto = new PaymentDto();
        dto.setOrderId("321");
        dto.setCustomerId("123");
        HttpEntity<PaymentDto> request = new HttpEntity<>(dto, headers);
        when(restTemplate.exchange(
                "http://localhost:8080/payment", HttpMethod.POST, request, Boolean.class))
                .thenReturn(ResponseEntity.of(Optional.of(true)));
        assertTrue(wsRaspayIntegration.processPayment(dto));
        verify(restTemplate,times(1)).exchange(
                "http://localhost:8080/payment", HttpMethod.POST, request, Boolean.class);

    }

    @Test
    void given_processPayment_when_apiResponseIs400BadRequest_then_returnNull() {
        ReflectionTestUtils.setField(wsRaspayIntegration, "raspayHost", "http://localhost:8080");
        ReflectionTestUtils.setField(wsRaspayIntegration, "paymentUrl", "/payment");
        PaymentDto dto = new PaymentDto();
        dto.setOrderId("321");
        dto.setCustomerId("123");
        HttpEntity<PaymentDto> request = new HttpEntity<>(dto, headers);
        when(restTemplate.exchange(
                "http://localhost:8080/payment", HttpMethod.POST, request, Boolean.class))
                .thenReturn(ResponseEntity.badRequest().build());
        assertNull(wsRaspayIntegration.processPayment(dto));
        verify(restTemplate,times(1)).exchange(
                "http://localhost:8080/payment", HttpMethod.POST, request, Boolean.class);

    }

    @Test
    void given_processPayment_when_apiResponseGetThrow_then_throwsHttpClientException() {
        ReflectionTestUtils.setField(wsRaspayIntegration, "raspayHost", "http://localhost:8080");
        ReflectionTestUtils.setField(wsRaspayIntegration, "paymentUrl", "/payment");
        PaymentDto dto = new PaymentDto();
        dto.setOrderId("321");
        dto.setCustomerId("123");
        HttpEntity<PaymentDto> request = new HttpEntity<>(dto, headers);
        when(restTemplate.exchange(
                "http://localhost:8080/payment", HttpMethod.POST, request, Boolean.class))
                .thenThrow(HttpClientException.class);
        assertThrows(HttpClientException.class, () -> wsRaspayIntegration.processPayment(dto));
        verify(restTemplate,times(1)).exchange(
                "http://localhost:8080/payment", HttpMethod.POST, request, Boolean.class);

    }

    private static HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String credentials = "rasmooplus:r@sm00";
        String base64 = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        headers.add("Authorization", "Basic "+base64);
        return headers;
    }
}