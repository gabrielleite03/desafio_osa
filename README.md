# 🚀 Spring Boot Application (Java 21 + Redis + Docker)

## 🧩 Visão Geral

Este projeto é uma aplicação **Spring Boot** construída com **Java 21**, integrando **Spring Security** para autenticação e **Spring Cache com Redis** para otimização de performance.  
O ambiente é totalmente **containerizado** utilizando **Docker Compose**, facilitando o deploy e a execução local.

---

## 🧱 Tecnologias Utilizadas

| Tecnologia | Descrição |
|-------------|------------|
| ☕ **Java 21** | Linguagem base do projeto |
| 🌀 **Spring Boot 3.x** | Framework principal da aplicação |
| 🔐 **Spring Security** | Autenticação e autorização |
| ⚡ **Spring Cache + Redis** | Cache distribuído para ganho de performance |
| 🐳 **Docker Compose** | Orquestração dos containers da aplicação |
| 🧰 **Maven** | Gerenciador de dependências e build |
| 🗄️ **Redis** | Armazenamento em memória usado como cache |

---

## 📂 Estrutura do Projeto

project-root/
│
├── src/
│ ├── main/java/... # Código fonte
│ └── main/resources/ # Configurações e application.yml
│
├── Dockerfile # Build da aplicação
├── docker-compose.yml # Orquestração dos containers
├── pom.xml # Dependências e build com Maven
└── README.md # Este arquivo



---

## ⚙️ Configuração do Ambiente

### 🔧 Pré-requisitos

Antes de iniciar, verifique se você possui instalado:

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/)
- [Java 21](https://adoptium.net/)
- [Maven 3.9+](https://maven.apache.org/)

---

## 🐳 Executando com Docker Compose

### 🏗️ 1. Construir e subir os containers

```bash
docker compose up --build
```
Isso irá:

Compilar a aplicação com Maven

Subir os containers da aplicação e do Redis

Mapear a porta 8080 para o host local

A aplicação estará acessível em:
http://localhost:8080

🔑 Autenticação (Spring Security)

A autenticação está configurada via Spring Security.
Por padrão, é possível definir as credenciais via variáveis de ambiente no docker-compose.yml ou no application.yml, por exemplo:
spring:
security:
user:
name: admin
password: admin123



1:
executar o login:

´´´
curl --location 'http://localhost:8080/auth/signin' \
--header 'Content-Type: application/json' \
--header 'Accept: application/json' \
--data '{
"username": "gabriel",
"password": "123456"

}'
´´´


2: 
Salvar localização da agência:
'''
curl --location 'http://localhost:8080/desafio/cadastrar' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6W10sImlhdCI6MTc2MTIzOTcyMSwiZXhwIjoxNzYxMjQzMzIxLCJzdWIiOiJnYWJyaWVsIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwIn0.BF_Ltd5SYnYf1GMMXXVKTRzGEsuRmJo1DK2aInj4fJo' \
--header 'Content-Type: application/json' \
--data '{
"posX": 10,
"posY": -5
}'
'''

3:
Consultar Agências:

curl --location 'http://localhost:8080/desafio/distancia?posX=15&posY=-4.5' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6W10sImlhdCI6MTc2MTIzOTcyMSwiZXhwIjoxNzYxMjQzMzIxLCJzdWIiOiJnYWJyaWVsIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwIn0.BF_Ltd5SYnYf1GMMXXVKTRzGEsuRmJo1DK2aInj4fJo'

