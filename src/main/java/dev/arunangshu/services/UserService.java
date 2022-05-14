package dev.arunangshu.services;

import dev.arunangshu.api.model.UserDto;
import dev.arunangshu.exceptions.ServiceException;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

import java.util.List;
import java.util.UUID;

public interface UserService {

    /**
     * Creates a new user.
     *
     * @param userDto the user object containing the user details
     * @return the created user
     * @throws ServiceException if persisting the user fails
     */
    UserDto createUser(UserDto userDto) throws ServiceException;

    /**
     * Gets the user with the given id.
     *
     * @param id the id of the user
     * @return the user with the given id
     */
    UserDto getUserById(UUID id);

    /**
     * Gets the user with the given nickname.
     *
     * @param nickname the unique nickname of the user
     * @return the user with the given nickname
     */
    UserDto getUserByNickname(String nickname);

    /**
     * Gets the user with the given email.
     *
     * @param email the email of the user
     * @return the user with the given email
     */
    UserDto getUserByEmail(String email);

    /**
     * Gets all the users.
     *
     * @return list of all the users
     */
    List<UserDto> getAllUsers();

    /**
     * Gets paged list of users.
     *
     * @param page object representing paging information
     * @return paged list of users
     */
    List<UserDto> getAllUsersPaged(Page page);

    /**
     * Gets sorted list of users.
     *
     * @param sort object representing sorting information
     * @return sorted list of users
     */
    List<UserDto> getAllUsersSorted(Sort sort);

    /**
     * Gets paged and sorted list of users.
     *
     * @param page object representing paging information
     * @param sort object representing sorting information
     * @return paged and sorted list of users
     */
    List<UserDto> getAllUsersPagedAndSorted(Page page, Sort sort);

    /**
     * Updates the user with the given id.
     *
     * @param id      the id of the user
     * @param userDto the user object containing the user details
     * @return the updated user
     */
    UserDto updateUserById(UUID id, UserDto userDto);

    /**
     * Updates the user with the given nickname.
     *
     * @param nickname the unique nickname of the user
     * @param userDto  the user object containing the user details
     * @return the updated user
     */
    UserDto updateUserByNickname(String nickname, UserDto userDto);

    /**
     * Updates the user with the given email.
     *
     * @param email   the email of the user
     * @param userDto the user object containing the user details
     * @return the updated user
     */
    UserDto updateUserByEmail(String email, UserDto userDto);

    /**
     * Deletes the user with the given id.
     *
     * @param id the id of the user
     */
    void deleteUserById(UUID id);

    /**
     * Deletes the user with the given nickname.
     *
     * @param nickname the unique nickname of the user
     */
    void deleteUserByNickname(String nickname);

    /**
     * Deletes the user with the given email.
     *
     * @param email the email of the user
     */
    void deleteUserByEmail(String email);
}
