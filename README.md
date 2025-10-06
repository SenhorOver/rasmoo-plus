# RASMOO PLUS

# Sobre o projeto

Rasmoo plus é um projeto backend, que simula todo o processo de um aplicativo de cursos que conta com assinatura paga para poder acessar o conteúdo.

Possui rotas protegidas com acesso limitado via Token (JWT) e cache integrado para fluxo de recuperação de senha e informações com baixa volatilidade.

## Modelo conceitual

![Modelo Conceitual](https://i.ibb.co/y94SG1B/Screenshot-from-2025-10-06-20-15-07.png)

# Tecnologias utilizadas

## Back end

- Java
- Spring Boot
- JPA / Hibernate
- Maven
- Hibernate Validator
- Spring Security
- JWT
- Flyway
- Redis
- Spring Mail
- MySql

# Como executar o projeto

Necessário rodar aplicação que simula o processo de pagamento, encontrada logo abaixo:

[FAKE API](https://github.com/FelipeDevRasmoo/ws-raspay#)

## Back end

Pré-requisitos: Java 21 e Docker

Importante:

- Necessário substituir informações de envio de email em application.properties

```bash
# clonar repositório
git clone https://github.com/SenhorOver/rasmoo-plus.git

# entrar na pasta do projeto back end
cd rasmoo-plus

# executar o projeto
docker compose up -d
./mvnw spring-boot:run
```

# Autor

Marcos Vinicius Silva

https://www.linkedin.com/in/marcos-v-s/
