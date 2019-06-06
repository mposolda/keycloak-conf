package org.keycloak.example;

import javax.ws.rs.core.MediaType;
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
        if (shopAttr == null) {
            challengeResponse(context, "No attribute 'favourite.shop' available on user");
            return;
        }

        if (!shopAttr.equalsIgnoreCase(shopParam)) {
            challengeResponse(context, "'" + shopParam + "' is not your favourite shop! It is: '" + shopAttr + "'");
            return;
        }

        context.success();
    }

    private void challengeResponse(AuthenticationFlowContext context, String error) {
        String accessCode = context.generateAccessCode();
        String actionUrl = context.getActionUrl(accessCode).toString();

        StringBuilder response = new StringBuilder("<html><head><title>Auth</title></head><body>");

        if (error != null) {
            response.append("ERROR: " + error);
        }

        response.append("<form method='POST' action='" + actionUrl + "'>");
        response.append(" What is your favourite shop? <input name='shop' /><br>");
        response.append(" <input type='submit' value='Submit' />");
        response.append("</form></body></html>");
        String html = response.toString();

        Response jaxrsResponse = Response
                .status(Response.Status.OK)
                .type("text/html")
                .entity(html)
                .build();

        context.challenge(jaxrsResponse);
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

