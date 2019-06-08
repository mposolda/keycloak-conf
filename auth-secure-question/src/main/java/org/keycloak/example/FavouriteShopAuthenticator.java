package org.keycloak.example;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

/**
 * @author <a href="mailto:mposolda@redhat.com">Marek Posolda</a>
 */
public class FavouriteShopAuthenticator implements Authenticator {

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        challengeResponse(context, null);
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> params = context.getHttpRequest().getDecodedFormParameters();
        String shopParam = params.getFirst("shop");

        if (shopParam == null) {
            challengeResponse(context, "Parameter 'shop' not presented");
            return;
        }

        UserModel user = context.getUser();
        if (user == null) {
            challengeResponse(context, "User was required from previous authenticators, but no user available");
            return;
        }

        String shopAttr = user.getFirstAttribute("favourite.shop");

        if (shopAttr == null || !shopAttr.equalsIgnoreCase(shopParam)) {
            challengeResponse(context, "'" + shopParam + "' is not your favourite shop!");
            return;
        }

        context.success();
    }

    private void challengeResponse(AuthenticationFlowContext context, String error) {
        if (error != null) {
            context.form().setError(error);
        }

        context.challenge(context.form().createForm("favourite-shop.ftl"));
    }

    @Override
    public boolean requiresUser() {
        return true;
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {

    }

    @Override
    public void close() {

    }

}

