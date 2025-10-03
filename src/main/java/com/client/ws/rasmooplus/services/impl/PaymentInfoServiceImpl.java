package com.client.ws.rasmooplus.services.impl;

import com.client.ws.rasmooplus.dto.PaymentProcessDto;
import com.client.ws.rasmooplus.dto.wsraspay.CustomerDto;
import com.client.ws.rasmooplus.dto.wsraspay.OrderDto;
import com.client.ws.rasmooplus.dto.wsraspay.PaymentDto;
import com.client.ws.rasmooplus.enums.UserTypeEnum;
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
import com.client.ws.rasmooplus.services.PaymentInfoService;
import com.client.ws.rasmooplus.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
public class PaymentInfoServiceImpl implements PaymentInfoService {
    @Value("${webservices.rasplus.default.password}")
    private String defaultPassword;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPaymentInfoRepository userPaymentInfoRepository;

    @Autowired
    private WsRaspayIntegration wsRaspayIntegration;

    @Autowired
    private MailIntegration mailIntegration;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private SubscriptionTypeRepository subscriptionTypeRepository;

    @Transactional
    @Override
    public Boolean process(PaymentProcessDto dto) {

        User user = userRepository.findById(dto.getUserPaymentInfoDto().getUserId()).orElseThrow(() -> new NotFoundException("User not found"));

        if(Objects.nonNull(user.getSubscriptionType())) {
            throw new BusinessException("Pagamento não pode ser processado pois usuário já possui assinatura");
        }

        Boolean successPayment = getSuccessPayment(dto, user);

        return createUserCredentials(dto, successPayment, user);
    }

    private boolean createUserCredentials(PaymentProcessDto dto, Boolean successPayment, User user) {
        if(Boolean.TRUE.equals(successPayment)) {

            UserPaymentInfo userPaymentInfo = UserPaymentInfoMapper.fromDtoToEntity(dto.getUserPaymentInfoDto(), user);
            userPaymentInfoRepository.save(userPaymentInfo);

            UserType userType = userTypeRepository.findById(UserTypeEnum.ALUNO.getId()).orElseThrow(() -> new NotFoundException("UserType not found!"));
            UserCredentials userCredentials = new UserCredentials(null, user.getEmail(), PasswordUtils.encode(defaultPassword), userType);
            userDetailsRepository.save(userCredentials);

            Optional<SubscriptionType> subscriptionTypeOpt = subscriptionTypeRepository.findByProductKey(dto.getProductKey());

            if(subscriptionTypeOpt.isEmpty()) {
                throw new NotFoundException("SubscriptionType not found");
            }

            user.setSubscriptionType(subscriptionTypeOpt.get());
            userRepository.save(user);

            mailIntegration.send(
                    user.getEmail(),
                    "Usuário: "+ user.getEmail() +" - Senha: "+ defaultPassword,
                    "Acesso Liberado!");
            return true;
        }
        return false;
    }

    private Boolean getSuccessPayment(PaymentProcessDto dto, User user) {
        CustomerDto customerDto = wsRaspayIntegration.createCustomer(CustomerMapper.build(user));
        OrderDto orderDto = wsRaspayIntegration.createOrder(OrderMapper.build(customerDto.getId(), dto));
        PaymentDto paymentDto = PaymentMapper.build(customerDto.getId(), orderDto.getId(), CreditCardMapper.build(dto.getUserPaymentInfoDto(), user.getCpf()));
        return wsRaspayIntegration.processPayment(paymentDto);
    }
}
