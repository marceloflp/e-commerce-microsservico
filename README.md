# 🛒 E-Commerce Microsserviços

Sistema de e-commerce desenvolvido com arquitetura de microsserviços em Java, composto por três serviços independentes que se comunicam entre si para gerenciar produtos e pedidos.

---

## 📐 Arquitetura

O projeto é dividido em dois microsserviços principais, cada um com sua própria responsabilidade de negócio:

```
e-commerce-microsservico/
├── Servico-Notificacao/  # Envia e-mail para cliente após finalizar pedido
├── Servico-Produtos/     # Gerenciamento do catálogo de produtos
└── Servico-Pedidos/      # Gerenciamento e processamento de pedidos
```

Cada serviço é independente, possui sua própria base de código e pode ser executado separadamente.

---

## 🧩 Microsserviços

### 📦 Serviço de Produtos (`Servico-Produtos`)

Responsável pelo gerenciamento do catálogo de produtos da loja. Expõe uma API REST para criação, consulta, atualização e remoção de produtos.

**Principais responsabilidades:**
- Cadastro e manutenção de produtos
- Consulta de produtos disponíveis
- Controle de informações como nome, preço e descrição

### 🧾 Serviço de Pedidos (`Servico-Pedidos`)

Responsável por receber e processar os pedidos realizados pelos clientes. Consome dados do Serviço de Produtos para validar e compor os pedidos.

**Principais responsabilidades:**
- Criação e consulta de pedidos
- Integração com o Serviço de Produtos
- Processamento e controle do ciclo de vida dos pedidos

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Descrição |
|---|---|
| **Java** | Linguagem principal do projeto (100% Java) |
| **Spring Boot** | Framework para criação dos microsserviços REST |
| **Spring Web** | Exposição das APIs RESTful |
| **RabbitMQ** | Comunicação assíncrona |
| **PostgreSQL** | Persistência dos dados no banco |
| **Spring Data JPA** | Persistência e mapeamento objeto-relacional |
| **MailHog** | Simular envio de e-mails |
| **Maven** | Gerenciamento de dependências e build |
| **WebClient / RestTemplate** | Comunicação HTTP entre os microsserviços |

---

## ▶️ Como Executar

### Pré-requisitos

- Java 17+ instalado
- Maven 3.8+ instalado
- Docker instalado

### Executando os serviços

Clone o repositório:

```bash
git clone https://github.com/marceloflp/e-commerce-microsservico.git
cd e-commerce-microsservico
```
Acesse a pasta do projeto e execute o seguinte comando para executar o docker:
```bash
docker compose up -d
```

Execute o **Serviço de Produtos** primeiro:

```bash
cd Servico-Produtos
mvn spring-boot:run
```

Em seguida, em outro terminal, execute o **Serviço de Pedidos**:

```bash
cd Servico-Pedidos
mvn spring-boot:run
```

Por fim, execute o **Serviço de Notificacao**:

```bash
cd Servico-Notificacao
mvn spring-boot:run
```

---

## 📡 Endpoints Principais

### Serviço de Produtos

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/produtos/buscarTodos` | Lista todos os produtos |
| `GET` | `/api/produtos/buscarPorId/id/{id}` | Busca produto por ID |
| `POST` | `/api/produtos/adicionar` | Cadastra um novo produto |
| `PUT` | `/api/produtos/aualizar/id/{id}` | Atualiza um produto |
| `DELETE` | `/api/produtos/deletar/id/{id}` | Remove um produto |

### Serviço de Pedidos

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/pedidos/buscarTodos` | Lista todos os pedidos |
| `GET` | `/api/pedidos/buscarPorId/id/{id}` | Busca pedido por ID |
| `POST` | `/api/pedidos/adicionar` | Cria um novo pedido |
| `PUT` | `/api/pedidos/atualizar/id/{id}` | Atualiza um pedido |
| `DELETE` | `/api/pedidos/deletar/id/{id}` | Remove um pedido |

---

## 🔗 Comunicação entre os Serviços

O **Serviço de Pedidos** realiza chamadas HTTP síncronas ao **Serviço de Produtos** para obter as informações necessárias durante a criação de um pedido, seguindo o padrão de integração entre microsserviços via API REST. Porém, a principal forma de comunicação entre todos os serviços é através do **RabbitMQ** utilizando comunicação assíncrona.

```
Cliente → Serviço de Pedidos ──HTTP──▶ Serviço de Produtos
Cliente → Serviço de Pedidos ──RabbitMQ──▶ Serviço de Produtos──▶RabbitMQ──▶ Serviço de Notificação
```

---

## 📁 Estrutura de Cada Serviço

Cada microsserviço segue a estrutura padrão de um projeto Spring Boot(com exceção do serviço de notificação):

```
Servico-Produtos/
└── src/
    └── main/
        ├── java/
        │   └── com/microsservico/
        |       ├── config/        # Configurações base
        |       ├── entities/      # Entidades/modelos
        │       ├── controller/    # Controladores REST
        │       ├── service/       # Regras de negócio
        │       ├── repository/    # Acesso ao banco de dados
        └── resources/
            └── application.properties
```
---

## 👤 Autor

Desenvolvido por [marceloflp](https://github.com/marceloflp).

