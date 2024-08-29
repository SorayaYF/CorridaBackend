# 🚀 Corrida Backend

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jwt&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-2C6AB1?style=for-the-badge&logo=jpa&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white)

**Corrida Backend** é uma API desenvolvida com Spring Boot e Java para suportar o front-end de uma aplicação Angular. Este backend lida com autenticação JWT, manipulação de dados usando JPA e Hibernate, e utiliza PostgreSQL como banco de dados.

---

## 📚 Descrição

O **Corrida Backend** é uma API que fornece funcionalidades para um aplicativo de corrida. As principais características incluem:

- **Autenticação JWT**: Protege as rotas da API e garante acesso seguro.
- **JPA e Hibernate**: Facilita a interação com o banco de dados PostgreSQL.
- **Testes Unitários com JUnit**: Garante a qualidade do código e o funcionamento das funcionalidades.

---

## 🚀 Como Executar

Para executar o **Corrida Backend** em sua máquina, siga os passos abaixo:

1. **Clone o repositório:**

   ```bash
   git clone https://github.com/SorayaYF/CorridaBackend.git
   cd CorridaBackend
   
2. **Configure o banco de dados:**

  - Instale o PostgreSQL se ainda não estiver instalado.
  - Configure o banco de dados e ajuste as propriedades de conexão no arquivo src/main/resources/application.properties.
  - Compile e execute o projeto:

3. **Compile e execute o projeto**

  - Certifique-se de ter o JDK e o Maven instalados. Em seguida, execute:
    
    ```bash
    mvn clean install
    mvn spring-boot:run
    
4. Integrar com o Front-end:

  - O front-end está disponível em SorayaYF/CorridaFrontend. Certifique-se de que o front-end está configurado para fazer chamadas para os endpoints da API.
   
---

## 🛠️ Tecnologias Utilizadas
**Java**: Linguagem de programação
**Spring Boot**: Framework para desenvolvimento de aplicações Java
**JUnit**: Framework para testes unitários
**PostgreSQL**: Sistema de gerenciamento de banco de dados relacional
**JWT**: Json Web Token para autenticação
**JPA & Hibernate**: ORM para interação com o banco de dados
