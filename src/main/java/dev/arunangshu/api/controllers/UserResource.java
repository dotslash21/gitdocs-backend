package dev.arunangshu.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.arunangshu.api.model.*;
import dev.arunangshu.exceptions.ServiceException;
import dev.arunangshu.services.UserService;
import dev.arunangshu.utils.NickNameGenerator;
import io.quarkus.oidc.UserInfo;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;
import java.util.stream.Collectors;

@Path(UserResource.API_PREFIX)
public class UserResource {

    public static final String API_PREFIX = "/api/v1/users";
    public static final String WHO_AM_I_PATH = "/whoami";
    public static final String REGISTER_PATH = "/register";

    public static final String USER_ID_PATH = "/{userId}";

    public static final String USER_NICKNAME_PATH = "/nickname/{nickname}";

    public static final String USER_EMAIL_PATH = "/email/{email}";

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
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @SecurityRequirement(name = "Auth0")
    public Response getAllUsers(@BeanParam PageRequest pageRequest, @BeanParam SortRequest sortRequest) {
        return Response.ok()
                .entity(userService.getAllUsersPagedAndSorted(
                        Page.of(pageRequest.getPage(), pageRequest.getSize()),
                        Sort.by(sortRequest.getSort(), sortRequest.getOrder())))
                .build();
    }

    @GET
    @Path(USER_ID_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @SecurityRequirement(name = "Auth0")
    public Response getUserById(@PathParam("userId") UUID userId) {
        return Response.ok()
                .entity(userService.getUserById(userId))
                .build();
    }

    @GET
    @Path(USER_NICKNAME_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @SecurityRequirement(name = "Auth0")
    public Response getUserByNickname(@PathParam("nickname") String nickname) {
        return Response.ok()
                .entity(userService.getUserByNickname(nickname))
                .build();
    }

    @GET
    @Path(USER_EMAIL_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @SecurityRequirement(name = "Auth0")
    public Response getUserByEmail(@PathParam("email") String email) {
        return Response.ok()
                .entity(userService.getUserByEmail(email))
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @SecurityRequirement(name = "Auth0")
    public Response createUser(UserDto userDto) throws ServiceException {
        return Response.ok()
                .entity(userService.createUser(userDto))
                .build();
    }

    @PUT
    @Path(USER_ID_PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @SecurityRequirement(name = "Auth0")
    public Response updateUserById(@PathParam("userId") UUID userId, UserDto userDto) {
        return Response.ok()
                .entity(userService.updateUserById(userId, userDto))
                .build();
    }

    @PUT
    @Path(USER_NICKNAME_PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @SecurityRequirement(name = "Auth0")
    public Response updateUserByNickname(@PathParam("nickname") String nickname, UserDto userDto) {
        return Response.ok()
                .entity(userService.updateUserByNickname(nickname, userDto))
                .build();
    }

    @PUT
    @Path(USER_EMAIL_PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @SecurityRequirement(name = "Auth0")
    public Response updateUserByEmail(@PathParam("email") String email, UserDto userDto) {
        return Response.ok()
                .entity(userService.updateUserByEmail(email, userDto))
                .build();
    }

    @DELETE
    @Path(USER_ID_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @SecurityRequirement(name = "Auth0")
    public Response deleteUserById(@PathParam("userId") UUID userId) {
        userService.deleteUserById(userId);
        return Response.ok().build();
    }

    @DELETE
    @Path(USER_NICKNAME_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @SecurityRequirement(name = "Auth0")
    public Response deleteUserByNickname(@PathParam("nickname") String nickname) {
        userService.deleteUserByNickname(nickname);
        return Response.ok().build();
    }

    @DELETE
    @Path(USER_EMAIL_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @SecurityRequirement(name = "Auth0")
    public Response deleteUserByEmail(@PathParam("email") String email) {
        userService.deleteUserByEmail(email);
        return Response.ok().build();
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
