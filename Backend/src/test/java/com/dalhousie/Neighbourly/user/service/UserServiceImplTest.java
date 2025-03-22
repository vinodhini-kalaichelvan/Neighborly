package com.dalhousie.Neighbourly.user.service;

import com.dalhousie.Neighbourly.user.entity.User;
import com.dalhousie.Neighbourly.user.entity.UserType;
import com.dalhousie.Neighbourly.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsUserPresentByEmail_returnsTrue() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        boolean result = userService.isUserPresent(email);
        assertTrue(result);
    }

    @Test
    void testIsUserPresentByEmail_returnsFalse() {
        String email = "absent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        boolean result = userService.isUserPresent(email);
        assertFalse(result);
    }

    @Test
    void testIsUserPresentById_returnsTrue() {
        int id = 1;
        when(userRepository.findById(id)).thenReturn(Optional.of(new User()));

        boolean result = userService.isUserPresent(id);
        assertTrue(result);
    }

    @Test
    void testIsUserPresentById_returnsFalse() {
        int id = 99;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        boolean result = userService.isUserPresent(id);
        assertFalse(result);
    }

    @Test
    void testFindUserByEmail() {
        String email = "user@example.com";
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserByEmail(email);
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testFindUserById() {
        int id = 5;
        User user = new User();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserById(id);
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testSaveUser() {
        User user = new User();
        userService.saveUser(user);

        verify(userRepository).save(user);
    }

    @Test
    void testUpdatePassword() {
        String email = "update@example.com";
        String newPassword = "newPass123";

        userService.updatePassword(email, newPassword);

        verify(userRepository).updatePassword(email, newPassword);
    }

    @Test
    void testIsUserPartOfAnyCommunity_returnsTrue() {
        User user = new User();
        user.setUserType(UserType.RESIDENT);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        boolean result = userService.isUserPartOfanyCommunity(1);
        assertTrue(result);
    }

    @Test
    void testIsUserPartOfAnyCommunity_returnsFalse_whenUserTypeIsNull() {
        User user = new User();
        user.setUserType(UserType.USER);
        when(userRepository.findById(2)).thenReturn(Optional.of(user));

        boolean result = userService.isUserPartOfanyCommunity(2);

        assertFalse(result);
    }
}

