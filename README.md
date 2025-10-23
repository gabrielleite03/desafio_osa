# spring_boot


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

