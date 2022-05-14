package dev.arunangshu.services;

import dev.arunangshu.api.mappers.UserMapper;
import dev.arunangshu.api.model.UserDto;
import dev.arunangshu.domain.User;
import dev.arunangshu.exceptions.ResourceNotFoundException;
import dev.arunangshu.exceptions.ServiceException;
import dev.arunangshu.repositories.UserRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import lombok.RequiredArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ApplicationScoped
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Creates a new user.
     *
     * @param userDto the user object containing the user details
     * @return the created user
     * @throws ServiceException if persisting the user fails
     */
    @Override
    public UserDto createUser(UserDto userDto) throws ServiceException {
        // 1. Persist the user
        userRepository.persist(userMapper.userDtoToUser(userDto));

        // 2. Return the persisted user
        return userRepository.find("email", userDto.getEmail())
                .firstResultOptional()
                .map(userMapper::userToUserDto)
                .orElseThrow(() -> new ServiceException("Error persisting user"));
    }

    /**
     * Gets the user with the given id.
     *
     * @param id the id of the user
     * @return the user with the given id
     */
    @Override
    public UserDto getUserById(UUID id) {
        return userRepository.findByIdOptional(id)
                .map(userMapper::userToUserDto)
                .orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Gets the user with the given nickname.
     *
     * @param nickname the unique nickname of the user
     * @return the user with the given nickname
     */
    @Override
    public UserDto getUserByNickname(String nickname) {
        return userRepository.find("nickname", nickname)
                .firstResultOptional()
                .map(userMapper::userToUserDto)
                .orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Gets the user with the given email.
     *
     * @param email the email of the user
     * @return the user with the given email
     */
    @Override
    public UserDto getUserByEmail(String email) {
        return userRepository.find("email", email)
                .firstResultOptional()
                .map(userMapper::userToUserDto)
                .orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Gets all the users.
     *
     * @return list of all the users
     */
    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    /**
     * Gets paged list of users.
     *
     * @param page object representing paging information
     * @return paged list of users
     */
    @Override
    public List<UserDto> getAllUsersPaged(Page page) {
        return userRepository.findAll()
                .page(page)
                .stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    /**
     * Gets sorted list of users.
     *
     * @param sort object representing sorting information
     * @return sorted list of users
     */
    @Override
    public List<UserDto> getAllUsersSorted(Sort sort) {
        return userRepository.findAll(sort)
                .stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    /**
     * Gets paged and sorted list of users.
     *
     * @param page object representing paging information
     * @param sort object representing sorting information
     * @return paged and sorted list of users
     */
    @Override
    public List<UserDto> getAllUsersPagedAndSorted(Page page, Sort sort) {
        return userRepository.findAll(sort)
                .page(page)
                .stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    /**
     * Updates the user with the given id.
     *
     * @param id      the id of the user
     * @param userDto the user object containing the user details
     * @return the updated user
     */
    @Override
    @Transactional
    public UserDto updateUserById(UUID id, UserDto userDto) {
        // 1. Get the user
        User user = userRepository.findByIdOptional(id)
                .orElseThrow(ResourceNotFoundException::new);

        // 2. Update the user
        user.setName(userDto.getName());
        user.setNickname(userDto.getNickname());
        user.setEmail(userDto.getEmail());
        user.setPicture(userDto.getPicture());

        return userMapper.userToUserDto(user);
    }

    /**
     * Updates the user with the given nickname.
     *
     * @param nickname the unique nickname of the user
     * @param userDto  the user object containing the user details
     * @return the updated user
     */
    @Override
    public UserDto updateUserByNickname(String nickname, UserDto userDto) {
        // 1. Get the user
        User user = userRepository.findByNicknameOptional(nickname)
                .orElseThrow(ResourceNotFoundException::new);

        // 2. Update the user
        user.setName(userDto.getName());
        user.setNickname(userDto.getNickname());
        user.setEmail(userDto.getEmail());
        user.setPicture(userDto.getPicture());

        return userMapper.userToUserDto(user);
    }

    /**
     * Updates the user with the given email.
     *
     * @param email   the email of the user
     * @param userDto the user object containing the user details
     * @return the updated user
     */
    @Override
    public UserDto updateUserByEmail(String email, UserDto userDto) {
        // 1. Get the user
        User user = userRepository.findByEmailOptional(email)
                .orElseThrow(ResourceNotFoundException::new);

        // 2. Update the user
        user.setName(userDto.getName());
        user.setNickname(userDto.getNickname());
        user.setEmail(userDto.getEmail());
        user.setPicture(userDto.getPicture());

        return userMapper.userToUserDto(user);
    }

    /**
     * Deletes the user with the given id.
     *
     * @param id the id of the user
     */
    @Override
    public void deleteUserById(UUID id) {
        User user = userRepository.findByIdOptional(id)
                .orElseThrow(ResourceNotFoundException::new);

        userRepository.delete(user);
    }

    /**
     * Deletes the user with the given nickname.
     *
     * @param nickname the unique nickname of the user
     */
    @Override
    public void deleteUserByNickname(String nickname) {
        User user = userRepository.findByNicknameOptional(nickname)
                .orElseThrow(ResourceNotFoundException::new);

        userRepository.delete(user);
    }

    /**
     * Deletes the user with the given email.
     *
     * @param email the email of the user
     */
    @Override
    public void deleteUserByEmail(String email) {
        User user = userRepository.findByEmailOptional(email)
                .orElseThrow(ResourceNotFoundException::new);

        userRepository.delete(user);
    }
}
