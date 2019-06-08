# Keycloak conference demo

Steps to have demo working
--------------------------
1) Unzip Keycloak 7.0.0-SNAPSHOT server distribution to some directory. Will be referenced as KEYCLOAK_HOME in next steps

2) Bootstrap Keycloak and go to `http://localhost:8080/auth` . Then create user `admin`

3) 
```
    mvn clean install
    cp $KEYCLOAK_HOME/standalone/deployments/
```

4) Setup the authenticator in some realm. New authentication flow needs to be created from existing 'browser' flow.
Add "Favourite Shop" authenticator as replacement of existing TOTP authenticator

5) Create the user with attribute "favourite.shop" of any value. This value will then need to be provided when this 
user wants to authenticate.

