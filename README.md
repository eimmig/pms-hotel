PMS Hotel
=========

Descrição
---------

Implementação de um software para gerenciamento de reservas, respeitando algumas regras de negócio como tarifas variáveis por dia da semana, cadastro de hóspede, quartos, categorias, tarifas e "comodidades" (extras).

Estrutura do Projeto
--------------------

O projeto é composto por três partes:

1.  **Frontend**: Um aplicativo em Angular 18 localizado na pasta `pms-hotel`.
2.  **Backend**: Um serviço geral que executa toda a parte lógica de negócio e funcionaldiades da aplicação, em Java 17 com Spring Boot e Maven localizado na pasta `booking`.
3.  **Serviço de E-mail**: Outro serviço em Java 17 com Spring Boot e Maven localizado na pasta `email` (para que o mesmo funcione é preciso configurar o SMTP).

### Requisitos

*   **Java**: 17
*   **Angular**: 18
*   **Banco de Dados**: PostgreSQL

Instalação
----------

Para instalar o projeto, siga os passos abaixo:

### 1\. Clonar o repositório

    git clone https://github.com/eimmig/pms-hotel.git

### 2\. Configurar o Banco de Dados

Certifique-se de ter o PostgreSQL instalado e configurado. Crie um banco de dados para o projeto.

### 3\. Executar os Projetos

Navegue até a pasta do backend e execute:

    cd booking
    mvn spring-boot:run

Navegue até a pasta do serviço de e-mail e execute:

    cd email
    mvn spring-boot:run

Por último, navegue até a pasta do frontend e execute:

    cd pms-hotel
    ng serve

Uso
---

Acesse o aplicativo frontend em `http://localhost:4200`. O backend estará disponível em `http://localhost:8081`.

Testes
------

Os testes estão implementados atualmente apenas no projeto de email. Para executar os testes, navegue até a pasta `email` e execute:

    cd email
    mvn test

Tecnologias e Ferramentas
-------------------------

*   **Backend**: Spring Boot, Spring Data JPA, RabbitMQ
*   **Frontend**: Angular, Material Design
*   **Banco de Dados**: PostgreSQL
*   **Java**: 17
*   **Outros**: Lombok

Roadmap
-------

*   Finalizar as telas para deixar o design correto.
*   Terminar as implementações com `//TODO`.
*   Ajustar o setup do Docker no projeto.
*   Implementar testes em todos os três projetos.

Documentação
------------

A documentação será gerada utilizando a coleção do Postman também está disponível no repositório.

[Postman Collection](https://www.postman.com/aerospace-candidate-71927754/af40957d-f75e-450d-8b3a-e9a0cac5ddee/share?collection=24093493-33ed3cfa-b143-4b23-ae9e-fd28f2aa7297&target=embed)
-------

[Acesso ao Diagrama de Classes](https://drive.google.com/file/d/1xoKYWodL1DlENsyWWGH5J5UmlHiTKYPZ/view?usp=sharing)
-------

[Acesso ao Diagrama do Banco de Dados](https://github.com/eimmig/pms-hotel/blob/main/diagrams/pms%20-%20public.png)
-------

<img title="Home Page" alt="Home Page" src="/images/start-page.png">
<img title="Amenities" alt="Amenities" src="/images/amenities-add.png">
