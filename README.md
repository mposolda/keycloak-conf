# Keycloak conference demo

Steps to have demo working
--------------------------
1) Unzip Keycloak 7.0.0-SNAPSHOT server distribution to some directory. Will be referenced as KEYCLOAK_HOME in next steps

2) Bootstrap Keycloak and go to `http://localhost:8080/auth` . Then create user `admin`

3) Login to admin console `http://localhost:8080/auth` and import demo realm from file `testrealm.json` .

4) 
```
    mvn clean install
    cp auth-secure-question/target/auth-secure-question-7.0.0-SNAPSHOT.jar $KEYCLOAK_HOME/standalone/deployments/
    cp themes/src/main/resources/theme/* $KEYCLOAK_HOME/themes/
```

4) Setup the authenticator in `demo` realm. New authentication flow needs to be created from existing 'browser' flow.
Add "Favourite Shop" authenticator as replacement of existing TOTP authenticator

5) Add the attribute "favourite.shop" to user `alice` of any value. This value will then need to be provided when this 
user wants to authenticate.

6) Change themes for login or account themes to sunrise/Keycloak-logo

