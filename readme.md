# Task tracker
## by DarkRubin
### 
#### Task tracker is single page application with microservice architecture, (backend, frontend, email-sender, scheduler)
#### Functional: registration, authorization, logout, create, read, finish, search, remove, update tasks, track time.
### Deploy: http://185.237.207.128
### Visual:
#### For self start you need:
- Clone project 
    ```
    git clone
    ```
- Create .env file with like this values
    ```
    PG_USERNAME=yourUsernameForDB
  
    PG_PASSWORD=yourPasswordForDB
  
    # You can generate own on https://jwtsecret.com/generate
    TOKEN_KEY=2d8a3c0d36f8c18543e259925a33d7ebde5229853b056c823da03230770f30243666601d8d37daa378072840123cedaa5ba555bbd5a8e5c461e5e6536477ac41
  
    MAIL_USERNAME=your@email.com
  
    # You need gennerate him on https://myaccount.google.com/apppasswords
    MAIL_PASSWORD=
  
    MAIL_HOST=smtp.gmail.com
    ```
- Run docker compose    
    ```
    docker compose -f compose.yaml up -d
    ```
- Wait while started all services and if you don't change compose.yaml, application will be available http://localhost/

#### Postman collection for test api
- Import ```TaskTrackerBackend.postman_collection.json``` collection in postman
- For test api in folder ```Tasks``` use Bearer authorization with token in header after registration


