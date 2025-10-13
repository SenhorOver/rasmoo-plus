package com.client.ws.rasmooplus.services;

import com.client.ws.rasmooplus.dto.PaymentProcessDto;
import com.client.ws.rasmooplus.dto.UserPaymentInfoDto;
import com.client.ws.rasmooplus.dto.wsraspay.CustomerDto;
import com.client.ws.rasmooplus.dto.wsraspay.OrderDto;
import com.client.ws.rasmooplus.dto.wsraspay.PaymentDto;
import com.client.ws.rasmooplus.exceptions.BusinessException;
import com.client.ws.rasmooplus.exceptions.NotFoundException;
import com.client.ws.rasmooplus.integration.MailIntegration;
import com.client.ws.rasmooplus.integration.WsRaspayIntegration;
import com.client.ws.rasmooplus.mapper.UserPaymentInfoMapper;
import com.client.ws.rasmooplus.mapper.wsraspay.CreditCardMapper;
import com.client.ws.rasmooplus.mapper.wsraspay.CustomerMapper;
import com.client.ws.rasmooplus.mapper.wsraspay.OrderMapper;
import com.client.ws.rasmooplus.mapper.wsraspay.PaymentMapper;
import com.client.ws.rasmooplus.model.jpa.*;
import com.client.ws.rasmooplus.repositories.jpa.*;
import com.client.ws.rasmooplus.services.impl.PaymentInfoServiceImpl;
import com.client.ws.rasmooplus.utils.PasswordUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class PaymentInfoServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserPaymentInfoRepository userPaymentInfoRepository;

    @Mock
    private WsRaspayIntegration wsRaspayIntegration;

    @Mock
    private MailIntegration mailIntegration;

    @Mock
    private UserDetailsRepository userDetailsRepository;

    @Mock
    private UserTypeRepository userTypeRepository;

    @Mock
    private SubscriptionTypeRepository subscriptionTypeRepository;

    @InjectMocks
    private PaymentInfoServiceImpl paymentInfoService;

    private static final Long USER_ID = 1L;
    private static final Long USER_TYPE_ID = 3L;
    private static final String DEFAULT_PASSWORD = "123";
    private static final String PRODUCT_KEY = "MONTH25";

    private PaymentProcessDto paymentProcessDto;
    private User user;
    private UserPaymentInfo userPaymentInfo;
    private UserType userType;
    private UserCredentials userCredentials;
    private SubscriptionType subscriptionType;

    private CustomerDto customerDto;
    private OrderDto orderDto;
    private PaymentDto paymentDto;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(paymentInfoService, "defaultPassword", DEFAULT_PASSWORD);
        paymentProcessDto = new PaymentProcessDto(PRODUCT_KEY, BigDecimal.ZERO, new UserPaymentInfoDto(
                1L,
                "1234123412345678",
                8L,
                2027L,
                "456",
                BigDecimal.TEN,
                0L,
                LocalDate.now(),
                USER_ID
        ));
        userType = new UserType(USER_TYPE_ID, "Aluno", "Descrição Aluno");
        subscriptionType = new SubscriptionType(1L, "MENSAL", 1, BigDecimal.valueOf(20),PRODUCT_KEY);
        user = new User(USER_ID, "Nome Sobrenome", "email@email.com", "11999998888", "44455566678", LocalDate.now(), LocalDate.now(), userType, subscriptionType);
        customerDto = CustomerMapper.build(user);
        orderDto = OrderMapper.build(customerDto.getId(), paymentProcessDto);
        paymentDto = PaymentMapper.build(customerDto.getId(), orderDto.getId(), CreditCardMapper.build(paymentProcessDto.getUserPaymentInfoDto(), user.getCpf()));

        userPaymentInfo = UserPaymentInfoMapper.fromDtoToEntity(paymentProcessDto.getUserPaymentInfoDto(), user);

        userCredentials = new UserCredentials(null, user.getEmail(), PasswordUtils.encode(DEFAULT_PASSWORD), userType);
    }

    @Test
    void given_process_when_dtoIsValidAndUserIsNotSubscribed_then_returnTrue() {
        user.setSubscriptionType(null);
        Mockito.when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        Mockito.when(wsRaspayIntegration.createCustomer(customerDto)).thenReturn(customerDto);
        Mockito.when(wsRaspayIntegration.createOrder(orderDto)).thenReturn(orderDto);
        Mockito.when(wsRaspayIntegration.processPayment(paymentDto)).thenReturn(true);
        Mockito.when(userPaymentInfoRepository.save(userPaymentInfo)).thenReturn(userPaymentInfo);
        Mockito.when(userTypeRepository.findById(USER_TYPE_ID)).thenReturn(Optional.of(userType));
        Mockito.when(userDetailsRepository.save(any())).thenReturn(userCredentials);
        Mockito.when(subscriptionTypeRepository.findByProductKey(PRODUCT_KEY)).thenReturn(Optional.of(subscriptionType));
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.doNothing().when(mailIntegration).send(any(), any(), any());

        assertEquals(true, paymentInfoService.process(paymentProcessDto));
    }

    @Test
    void given_process_when_dtoIsValidAndUserIsSubscribed_then_throwsBusinessException() {
        Mockito.when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        assertThrows(BusinessException.class,() -> paymentInfoService.process(paymentProcessDto));
    }

    @Test
    void given_process_when_dtoIsNotValidWhenUserTypeNotFoundAndUserIsNotSubscribed_then_throwsNotFoundException() {
        user.setSubscriptionType(null);
        Mockito.when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        Mockito.when(wsRaspayIntegration.createCustomer(customerDto)).thenReturn(customerDto);
        Mockito.when(wsRaspayIntegration.createOrder(orderDto)).thenReturn(orderDto);
        Mockito.when(wsRaspayIntegration.processPayment(paymentDto)).thenReturn(true);
        Mockito.when(userPaymentInfoRepository.save(userPaymentInfo)).thenReturn(userPaymentInfo);
        Mockito.when(userTypeRepository.findById(USER_TYPE_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,() -> paymentInfoService.process(paymentProcessDto));
    }

    @Test
    void given_process_when_dtoIsNotValidWhenSubscriptionTypeNotFoundAndUserIsNotSubscribed_then_throwsNotFoundException() {
        user.setSubscriptionType(null);
        Mockito.when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        Mockito.when(wsRaspayIntegration.createCustomer(customerDto)).thenReturn(customerDto);
        Mockito.when(wsRaspayIntegration.createOrder(orderDto)).thenReturn(orderDto);
        Mockito.when(wsRaspayIntegration.processPayment(paymentDto)).thenReturn(true);
        Mockito.when(userPaymentInfoRepository.save(userPaymentInfo)).thenReturn(userPaymentInfo);
        Mockito.when(userTypeRepository.findById(USER_TYPE_ID)).thenReturn(Optional.of(userType));
        Mockito.when(userDetailsRepository.save(any())).thenReturn(userCredentials);
        Mockito.when(subscriptionTypeRepository.findByProductKey(PRODUCT_KEY)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,() -> paymentInfoService.process(paymentProcessDto));
    }

    @Test
    void given_process_when_integrationFailed_then_returnFalse() {
        user.setSubscriptionType(null);
        Mockito.when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        Mockito.when(wsRaspayIntegration.createCustomer(customerDto)).thenReturn(customerDto);
        Mockito.when(wsRaspayIntegration.createOrder(orderDto)).thenReturn(orderDto);
        Mockito.when(wsRaspayIntegration.processPayment(paymentDto)).thenReturn(false);

        assertEquals(false, paymentInfoService.process(paymentProcessDto));
    }
}