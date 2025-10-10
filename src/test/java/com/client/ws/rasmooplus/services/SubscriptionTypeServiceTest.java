package com.client.ws.rasmooplus.services;

import com.client.ws.rasmooplus.dto.SubscriptionTypeDto;
import com.client.ws.rasmooplus.exceptions.BadRequestException;
import com.client.ws.rasmooplus.exceptions.NotFoundException;
import com.client.ws.rasmooplus.model.jpa.SubscriptionType;
import com.client.ws.rasmooplus.repositories.jpa.SubscriptionTypeRepository;
import com.client.ws.rasmooplus.services.impl.SubscriptionTypeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionTypeServiceTest {

    @Mock
    private SubscriptionTypeRepository subscriptionTypeRepository;

    @InjectMocks
    private SubscriptionTypeServiceImpl subscriptionTypeService;

    private static final Long ID = 1L;

    private SubscriptionType subscriptionType;
    private SubscriptionTypeDto subscriptionTypeDto;

    @BeforeEach
    void setUp() {
        subscriptionType = new SubscriptionType(ID, "Plano Mensal", 1, BigDecimal.valueOf(20), "MONTH25");
        subscriptionTypeDto = getSubscriptionTypeDtos(subscriptionType);
    }

    @Test
    void given_findAll_then_returnSubscriptions() {
        List<SubscriptionType> subscriptionTypeList = new ArrayList<>();
        subscriptionTypeList.add(subscriptionType);
        Mockito.when(subscriptionTypeRepository.findAll()).thenReturn(subscriptionTypeList);

        List<SubscriptionTypeDto> subscriptionTypeDtoList = new ArrayList<>();
        subscriptionTypeDtoList.add(getSubscriptionTypeDtos(subscriptionType));

        Assertions.assertEquals(subscriptionTypeDtoList, subscriptionTypeService.findAll());
    }

    @Test
    void given_findById_when_idIsValid_then_returnSubscriptions() {
        Mockito.when(subscriptionTypeRepository.findById(ID)).thenReturn(Optional.of(subscriptionType));

        assertEquals(subscriptionTypeDto, subscriptionTypeService.findById(ID));
    }

    @Test
    void given_findById_when_idIsNotValid_then_throwsNotFoundException() {
        Mockito.when(subscriptionTypeRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> subscriptionTypeService.findById(ID));
    }

    @Test
    void given_create_when_idIsNull_then_returnCreatedSubscription() {
        subscriptionType.setId(null);
        Mockito.when(subscriptionTypeRepository.save(subscriptionType)).thenReturn(subscriptionType);

        SubscriptionTypeDto subscriptionTypeDtoWithoutId = getSubscriptionTypeDtos(subscriptionType);
        subscriptionTypeDtoWithoutId.setId(null);

        SubscriptionTypeDto subscriptionTypeDtoWithId = getSubscriptionTypeDtos(subscriptionType);
        assertEquals(subscriptionTypeDtoWithId, subscriptionTypeService.create(subscriptionTypeDtoWithoutId));
    }

    @Test
    void given_create_when_idIsNotNull_then_throwsBadRequestException() {
        assertThrows(BadRequestException.class, () -> subscriptionTypeService.create(subscriptionTypeDto));
    }

    @Test
    void given_update_when_idIsFound_then_returnUpdateSubscription() {
        Mockito.when(subscriptionTypeRepository.findById(ID)).thenReturn(Optional.of(subscriptionType));
        Mockito.when(subscriptionTypeRepository.save(subscriptionType)).thenReturn(subscriptionType);

        assertEquals(subscriptionTypeDto, subscriptionTypeService.update(ID, subscriptionTypeDto));
    }

    @Test
    void given_update_when_idIsNotFound_then_returnUpdateSubscription() {
        Mockito.when(subscriptionTypeRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> subscriptionTypeService.update(ID, subscriptionTypeDto));
    }

    @Test
    void given_delete_when_IdIsFound_then_returnVoid() {
        Mockito.when(subscriptionTypeRepository.findById(ID)).thenReturn(Optional.of(subscriptionType));
        Mockito.doNothing().when(subscriptionTypeRepository).deleteById(ID);

        assertDoesNotThrow(() -> subscriptionTypeService.delete(ID));
    }

    @Test
    void given_delete_when_IdIsNotFound_then_returnVoid() {
        Mockito.when(subscriptionTypeRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> subscriptionTypeService.delete(ID));
    }

    private SubscriptionTypeDto getSubscriptionTypeDtos(SubscriptionType subscriptionType) {
        subscriptionTypeDto = new SubscriptionTypeDto();
        subscriptionTypeDto.setId(subscriptionType.getId());
        subscriptionTypeDto.setName(subscriptionType.getName());
        subscriptionTypeDto.setPrice(subscriptionType.getPrice());
        subscriptionTypeDto.setProductKey(subscriptionType.getProductKey());
        subscriptionTypeDto.setAccessMonths(subscriptionType.getAccessMonths());
        return subscriptionTypeDto;
    }
}