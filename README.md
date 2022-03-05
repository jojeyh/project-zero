# Project 0: A Simple Banking REST API

## Description

This is a simple banking api that updates the balance of client accounts and returns information to the user.  It handles HTTP requests through a RESTful API and returns information back to the user via the command line.

## Endpoint Requirements

- `POST /clients`
- `GET /clients`
- `GET /clients/{client_id}`
- `PUT /clients/{client_id}`
- `DELETE /clients/{client_id}`
- `POST /clients/{client_id}/accounts`
    - `GET /clients/{client_id}/accounts`
    - `GET /clients/{client_id}/accounts?amountLessThan=M&amountGreaterThan=N`
- `GET /clients/{client_id}/accounts/{account_id}`
- `PUT /clients/{client_id}/accounts/{account_id}`
- `DELETE /clients/{client_id}/accounts/{account_id}`

## Architecture Design
The design is a minimal version of a Model-View-Controller-Service (MVCS) that consists of a Controller, Service and Data Access Object (DAO). 

`User/Client <--> Controller <--> Service Layer <--> Data Access Layer <--> Server`

The controller handles incoming HTTP requests and passes them off to the service layer, which contains the business logic corrensponding to each endpoint's request.  The data access layer handles CRUD operations and acts as a gatekeeper between the database and the service layer where the core application logic lives.
