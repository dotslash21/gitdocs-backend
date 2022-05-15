package dev.arunangshu.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.arunangshu.api.model.RequestStatus;
import dev.arunangshu.api.model.RequestStatusWrapper;
import dev.arunangshu.api.model.UserDto;
import dev.arunangshu.api.model.WhoAmI;
import dev.arunangshu.services.UserService;
import dev.arunangshu.utils.NickNameGenerator;
import io.quarkus.oidc.UserInfo;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

@Path(UserResource.API_PREFIX)
public class UserResource {

    public static final String API_PREFIX = "/api/v1/users";
    public static final String WHO_AM_I_PATH = "/whoami";
    public static final String REGISTER_PATH = "/register";

    private final UserInfo userInfo;
    private final String roleClaimPath;
    private final ObjectMapper objectMapper;
    private final UserService userService;

    public UserResource(
            UserInfo userInfo,
            @ConfigProperty(name = "quarkus.oidc.roles.role-claim-path") String roleClaimPath,
            ObjectMapper objectMapper, UserService userService) {

        this.userInfo = userInfo;
        this.roleClaimPath = roleClaimPath;
        this.objectMapper = objectMapper;
        this.userService = userService;
    }

    @GET
    @Path(WHO_AM_I_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @SecurityRequirement(name = "Auth0")
    public Response whoAmI() {
        return Response.ok()
                .entity(WhoAmI.builder()
                        .name(userInfo.getString("name"))
                        .roles(userInfo.getArray(roleClaimPath.replace("\"", ""))
                                .stream()
                                .map(jsonValue -> jsonValue.toString().replace("\"", ""))
                                .collect(Collectors.toList()))
                        .build())
                .build();
    }

    @GET
    @Path(REGISTER_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @SecurityRequirement(name = "Auth0")
    public Response register() {
        try {
            UserDto userDto = objectMapper.readValue(
                    userInfo.getUserInfoString(), UserDto.class);

            if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
                throw new IllegalArgumentException("Email is mandatory for registering");
            }

            if (userDto.getNickname() == null || userDto.getNickname().isEmpty()) {
                userDto.setNickname(NickNameGenerator.fromEmail(userDto.getEmail()));
            }

            userService.createUser(userDto);

            return Response.ok()
                    .entity(RequestStatusWrapper.builder()
                            .status(RequestStatus.SUCCESS)
                            .message("User registered successfully")
                            .build())
                    .build();
        } catch (Exception e) {
            return Response.serverError()
                    .entity(RequestStatusWrapper.builder()
                            .status(RequestStatus.FAILURE)
                            .message(e.getLocalizedMessage())
                            .build())
                    .build();
        }
    }
}
