# PP-CMS

## 2024.02
> Project was refactored as much as possible without touching frontend codes.
> Todo: add unit tests

The server part of the Multilangual Content Management System project implemented for the Content Management Systems subject in the second semester of the Master's degree studies at the Poznań University of Technology.

The system was developed in Java, Spring Boot framework, JPA, Spring Security, Spring Session, REST API. This release uses the H2 database.

The system offers:
```
- Register new users
- Login to the service
- User managenemt
- Roles and privileges
- Roles and privileges management (adding Roles and assigning authorities to them)
- Basic Authentication
- Creating articles
- Articles management
- Creating languages and translation for components on frontend (Language, AlertCode and AlertTranslation models)
- Adding comments to published articles
- Moderating comments
- ConfigurationFlags (to turn on/off comments, login and register)
- SetupDataLoader (remember to change flag alreadySetup to false during first run to generate Database in SetupDataLoader class)
- CORS configuration (remember to setup cors in custom annotation interface CustomCorsConfigAnnotation)
- In case of security some endpoints are secured by authority of users, to find these endpoints go to SecurityConfiguration class. 
```

## Frontend
> Dedicated frontend for this system can be found here https://github.com/kamkow10/pp-cms-frontend/tree/prod

## Documentation
> Documentation in Polish language is available here https://drive.google.com/file/d/1BJfAsnfLvQDqr7-gusWHOJ9z9vSgTYS2/view?usp=sharing

## Installation
> Install project using docker files from this repo https://github.com/mariski701/pp-cms-docker
