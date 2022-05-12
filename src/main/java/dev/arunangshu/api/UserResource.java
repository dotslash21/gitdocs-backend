package dev.arunangshu.api;

import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.Set;

@Path(UserResource.API_PREFIX)
public class UserResource {

    public static final String API_PREFIX = "/api/v1/users";
    public static final String ME_PATH = "/me";

    private final SecurityContext securityContext;
    private final JsonWebToken idToken;
    private final String namespace;

    public UserResource(
            SecurityContext securityContext,
            JsonWebToken idToken,
            @ConfigProperty(name = "dev.arunangshu.token.claim.namespace") String namespace) {

        this.securityContext = securityContext;
        this.idToken = idToken;
        this.namespace = namespace;
    }

    @GET
    @Path(ME_PATH)
    @Produces(MediaType.TEXT_PLAIN)
    @Authenticated
    public String getMe() {
        return "Hello, "
                + securityContext.getUserPrincipal().getName()
                + " userInfo: "
                + idToken.getClaim(String.format("%s/userinfo", namespace));
    }
}
