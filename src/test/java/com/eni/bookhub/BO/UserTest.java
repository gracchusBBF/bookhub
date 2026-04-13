package com.eni.bookhub.BO;

import com.eni.bookhub.repository.UserRepository;
import com.eni.bookhub.repository.UserRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Slf4j
public class UserTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Test
    void addUser() {
        final UserRole userRole = UserRole
                .builder()
                .roleName("ROLE_USER")
                .build();
        final UserRole savedUserRole = userRoleRepository.save(userRole);
        log.info("Saved role: " + savedUserRole.toString());

        final User user = User
                .builder()
                .email("testUser@gmail.com")
                .password("123456")
                .firstName("TestFirstName")
                .lastName("TestLastName")
                .phoneNumber("123456789")
                .userRole(savedUserRole)
                .build();

        final User savedUser = userRepository.save(user);
        log.info("Saved user: " + savedUser.toString());

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.equals(user)).isTrue();
    }

    @Test
    void findUserById() {
        final UserRole userRole = UserRole
                .builder()
                .roleName("ROLE_USER")
                .build();
        final UserRole savedUserRole = userRoleRepository.save(userRole);
        log.info("Saved role: " + savedUserRole.toString());

        final User user = User
                .builder()
                .email("testUser@gmail.com")
                .password("123456")
                .firstName("TestFirstName")
                .lastName("TestLastName")
                .phoneNumber("123456789")
                .userRole(savedUserRole)
                .build();
        final User savedUser = userRepository.save(user);

        final Optional<User> userById = userRepository.findById(savedUser.getId());
        log.info("Find user by email: " + userById.toString());
        assertThat(userById).isPresent();
    }

    @Test
    public void findUserByEmail() {
        final UserRole userRole = UserRole
                .builder()
                .roleName("ROLE_USER")
                .build();
        final UserRole savedUserRole = userRoleRepository.save(userRole);
        log.info("Saved role: " + savedUserRole.toString());

        final User user = User
                .builder()
                .email("testUser@gmail.com")
                .password("123456")
                .firstName("TestFirstName")
                .lastName("TestLastName")
                .phoneNumber("123456789")
                .userRole(savedUserRole)
                .build();
        final User savedUser = userRepository.save(user);

        final Optional<User> userByEmail = userRepository.getUserByEmail(savedUser.getEmail());
        log.info("Find user by email: " + userByEmail.toString());
        assertThat(userByEmail).isPresent();
    }

    @Test
    public void modifyUserEmail() {
        final UserRole userRole = UserRole
                .builder()
                .roleName("ROLE_USER")
                .build();
        final UserRole savedUserRole = userRoleRepository.save(userRole);
        log.info("Saved role: " + savedUserRole.toString());

        final User user = User
                .builder()
                .email("testUser@gmail.com")
                .password("123456")
                .firstName("TestFirstName")
                .lastName("TestLastName")
                .phoneNumber("123456789")
                .userRole(savedUserRole)
                .build();
        User savedUser = userRepository.save(user);

        // Modification
        user.setEmail("modifiedEmail@gmail.com");
        savedUser = userRepository.save(user);
        log.info("Modified email: " +  savedUser.getEmail());

        final Optional<User> userByEmail = userRepository.getUserByEmail(savedUser.getEmail());
        log.info("Find user by email: " + userByEmail.toString());
        assertThat(userByEmail).isPresent();
    }

    @Test
    public void deleteUser() {
        final UserRole userRole = UserRole
                .builder()
                .roleName("ROLE_USER")
                .build();
        final UserRole savedUserRole = userRoleRepository.save(userRole);
        log.info("Saved role: " + savedUserRole.toString());

        final User user = User
                .builder()
                .email("testUser@gmail.com")
                .password("123456")
                .firstName("TestFirstName")
                .lastName("TestLastName")
                .phoneNumber("123456789")
                .userRole(savedUserRole)
                .build();
        User savedUser = userRepository.save(user);

        userRepository.deleteUserById(savedUser.getId());
        log.info("Delete user...");
        Optional<User> userByEmail = userRepository.getUserByEmail(savedUser.getEmail());
        Assertions.assertThat(userByEmail).isEmpty();
    }
}
