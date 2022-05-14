package dev.arunangshu.repositories;

import dev.arunangshu.domain.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@Transactional
class UserRepositoryTest {

    @Inject
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        List<User> users = IntStream.range(1, 11)
                .mapToObj(i -> User.builder()
                        .name(String.format("user-%d", i))
                        .nickname(String.format("user-%d", i))
                        .email(String.format("user-%d@example.com", i))
                        .picture(String.format("https://example.com/%d.png", i))
                        .build())
                .collect(Collectors.toList());

        userRepository.persist(users);
    }

    @AfterEach
    void tearDown() {
        userRepository.findAll().stream().forEach(PanacheEntityBase::delete);
    }

    @Test
    void findAll() {
        List<User> users = userRepository.findAll().list();

        assertEquals(10, users.size());
    }

    @Test
    void findByNicknameOptional() {
        Optional<User> user = userRepository.findByNicknameOptional("user-7");

        assertTrue(user.isPresent());
        assertEquals("user-7", user.get().getName());
        assertEquals("user-7", user.get().getNickname());
        assertEquals("user-7@example.com", user.get().getEmail());
        assertEquals("https://example.com/7.png", user.get().getPicture());
    }

    @Test
    void findByEmailOptional() {
        Optional<User> user = userRepository.findByEmailOptional("user-5@example.com");

        assertTrue(user.isPresent());
        assertEquals("user-5", user.get().getName());
        assertEquals("user-5", user.get().getNickname());
        assertEquals("user-5@example.com", user.get().getEmail());
        assertEquals("https://example.com/5.png", user.get().getPicture());
    }
}