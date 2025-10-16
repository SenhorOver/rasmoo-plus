package com.client.ws.rasmooplus.repositories.redis;


import com.client.ws.rasmooplus.model.redis.UserRecoveryCode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.AutoConfigureDataRedis;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureTestDatabase
@AutoConfigureDataRedis
@WebMvcTest(UserRecoveryCodeRepository.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(profiles = "test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRecoveryCodeRepositoryTest {

    @Autowired
    private UserRecoveryCodeRepository userRecoveryCodeRepository;

    @BeforeAll
    void loadSubscriptions() {
        List<UserRecoveryCode> userRecoveryCodeList = new ArrayList<>();
        UserRecoveryCode userRecoveryCode1 = new UserRecoveryCode();
        userRecoveryCode1.setEmail("email1@email.com");
        userRecoveryCode1.setCode("0001");
        UserRecoveryCode userRecoveryCode2 = new UserRecoveryCode();
        userRecoveryCode2.setEmail("email2@email.com");
        userRecoveryCode2.setCode("0002");
        UserRecoveryCode userRecoveryCode3 = new UserRecoveryCode();
        userRecoveryCode3.setEmail("email3@email.com");
        userRecoveryCode3.setCode("0003");
        userRecoveryCodeList.add(userRecoveryCode1);
        userRecoveryCodeList.add(userRecoveryCode2);
        userRecoveryCodeList.add(userRecoveryCode3);

        userRecoveryCodeRepository.saveAll(userRecoveryCodeList);
    }

    @AfterAll
    void dropDatabase() {
        userRecoveryCodeRepository.deleteAll();
    }

    @Test
    void given_findByEmail_when_getByEmail_then_returnCorrectUserRecoveryCode() {
        assertEquals("0001", userRecoveryCodeRepository.findByEmail("email1@email.com").get().getCode());
        assertEquals("0002", userRecoveryCodeRepository.findByEmail("email2@email.com").get().getCode());
        assertEquals("0003", userRecoveryCodeRepository.findByEmail("email3@email.com").get().getCode());
    }
}