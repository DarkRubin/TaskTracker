# Task tracker
## by DarkRubin
### ![java_podgon1 (2)](https://github.com/user-attachments/assets/f663d5ec-8eed-4394-b663-516cb97ad741) ![Spring boot (2)](https://github.com/user-attachments/assets/ff9ef5f7-a89b-421a-9c2c-2fe86f13c849) ![Spring Data JPA (2)](https://github.com/user-attachments/assets/9f084b16-b113-42d8-8fd2-5113b541b285) ![Spring Security (2)](https://github.com/user-attachments/assets/4a940281-d015-49eb-8dda-cfd5922336ac) ![Liquibase (2)](https://github.com/user-attachments/assets/3df9d620-4b38-4d08-9341-f96198205a63) ![Gradle](https://github.com/user-attachments/assets/dc5180c0-bf2d-4352-9c0c-ed5e1b0369af) ![dockerfile (2)](https://github.com/user-attachments/assets/3065776b-58bc-4878-a905-3022d8067358) ![docker-compose (2)](https://github.com/user-attachments/assets/5703fae2-abd9-4811-9493-3aef173d6cc3)

#### Task tracker is single page application with microservice architecture, (backend, frontend, email-sender, scheduler)
#### Functional: registration, authorization, logout, create, read, finish, search, remove, update tasks, track time.
### Visual:![Знімок екрана 2024-10-18 073424](https://github.com/user-attachments/assets/3431f84d-928a-41cc-b45d-c34f24fb2717)
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


