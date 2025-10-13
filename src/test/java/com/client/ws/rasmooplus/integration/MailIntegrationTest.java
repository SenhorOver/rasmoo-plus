package com.client.ws.rasmooplus.integration;

import com.client.ws.rasmooplus.integration.impl.MailIntegrationImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MailIntegrationTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private MailIntegrationImpl mailIntegration;

    @Test
    void given_send_when_dataIsValid_then_sendMail() {
        String mailTo = "email@email.com";
        String message = "Mensagem";
        String subject = "Assunto";
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(mailTo);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        Mockito.doNothing().when(javaMailSender).send(simpleMailMessage);

        assertDoesNotThrow(() -> mailIntegration.send(mailTo, message, subject));

        Mockito.verify(javaMailSender, Mockito.times(1)).send(simpleMailMessage);
    }
}