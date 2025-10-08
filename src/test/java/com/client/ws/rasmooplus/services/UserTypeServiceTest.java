package com.client.ws.rasmooplus.services;

import com.client.ws.rasmooplus.model.jpa.UserType;
import com.client.ws.rasmooplus.repositories.jpa.UserTypeRepository;
import com.client.ws.rasmooplus.services.impl.UserTypeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;


//@SpringBootTest // Usado quando é necessário o contexto do Spring
@ExtendWith(MockitoExtension.class)
class UserTypeServiceTest {

    //@MockitoBean // Usado quando o contexto do Spring estiver rodando (testar controller, integração, etc)
    // @Mock - Usado quando o teste não preciso do contexto
    @Mock
    private UserTypeRepository userTypeRepository;


    // @Autowired // Não funciona @Autowired sem contexto
    // private UserTypeService userTypeService;
    @InjectMocks // Utilizo InjectMocks para inserir o conteúdo aqui e poder ser utilizado sem o contexto do spring
    private UserTypeServiceImpl userTypeService; // Ao não trabalhar com context, tenho que chamar a implementação

    // given_metodo_when_cenario_then_retornoEsperado
    @Test
    void given_findAll_when_thereAreDataInDatabase_then_returnAllData() {
        List<UserType> userTypeList = new ArrayList<>();
        UserType userType1 = new UserType(1L, "Professor", "Professor da plataforma");
        UserType userType2 = new UserType(2L, "Administador", "Funcionário da plataforma");

        userTypeList.add(userType1);
        userTypeList.add(userType2);

        Mockito.when(userTypeRepository.findAll()).thenReturn(userTypeList);
        var result = userTypeService.findAll();
        Assertions.assertThat(result).isNotEmpty().hasSize(2);
    }
}
