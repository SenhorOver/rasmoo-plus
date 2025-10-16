package com.client.ws.rasmooplus.repositories.jpa;

import com.client.ws.rasmooplus.model.jpa.UserCredentials;
import com.client.ws.rasmooplus.model.jpa.UserType;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@WebMvcTest(UserDetailsRepository.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(profiles = "test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDetailsRepositoryTest {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @BeforeAll
    void loadUserDetails() {
        List<UserCredentials> userCredentialsList = new ArrayList<>();
        UserCredentials userCredentials1 = new UserCredentials(null, "email1@email.com", "password1234", null);
        UserCredentials userCredentials2 = new UserCredentials(null, "email2@email.com", "password1234", null);
        UserCredentials userCredentials3 = new UserCredentials(null, "email3@email.com", "password1234", null);
        userCredentialsList.add(userCredentials1);
        userCredentialsList.add(userCredentials2);
        userCredentialsList.add(userCredentials3);

        userDetailsRepository.saveAll(userCredentialsList);
    }

    @AfterAll
    void dropDatabase() {
        userDetailsRepository.deleteAll();
    }

    @Test
    void given_findByUsername_when_getUsername_then_returnUserCredentials() {
       assertEquals("email1@email.com", userDetailsRepository.findByUsername("email1@email.com").get().getUsername());
       assertEquals("email2@email.com", userDetailsRepository.findByUsername("email2@email.com").get().getUsername());
       assertEquals("email3@email.com", userDetailsRepository.findByUsername("email3@email.com").get().getUsername());
    }

}