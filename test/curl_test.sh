#!/bin/sh
echo "Printing users"
curl -X GET http://localhost:8080/api/users

echo "Creating user01"
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "fName": "xyz",
    "lName": "abc",
    "username": "xyz123",
    "password": "xyzpass",
    "email": "xyz@gmail.com"
  }'


echo "Creating user02"
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "fName": "user02",
    "lName": "user02Lname",
    "username": "user02_123",
    "password": "user02_pass",
    "email": "user02@gmail.com"
  }'

echo "Printing users"
curl -X GET http://localhost:8080/api/users | jq .

echo "Creating todo item"
curl -X POST http://localhost:8080/api/items \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Title01",
    "description": "Title01_desc",
    "createdById" : 1,
    "assignedToId": 2
  }'

curl -X POST http://localhost:8080/api/items \
    -H "Content-Type: application/json" \
    -d '{
      "title": "Title02",
      "description": "Title02_desc",
      "createdById" : 2,
      "assignedToId": 1
    }'

echo "Printing todo items"
curl -X GET http://localhost:8080/api/items | jq .

cur