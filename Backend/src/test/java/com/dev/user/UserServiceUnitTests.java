package com.dev.user;

import com.dev.auth.Role;
import com.dev.auth.User;
import com.dev.auth.UserRepository;
import com.dev.auth.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTests {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    // CREATE
    @Test
    void testAddNewUser_Success() {
        String username = "newuser";
        String password = "password";
        Role role = Role.USER;
        when(userRepository.findUserByUsername(username)).thenReturn(Optional.empty());
        User newUser = new User();
        newUser.setId(1);
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        int result = userService.addNewUser(username, password, role);
        assertEquals(1, result);
        verify(userRepository, times(1)).save(any(User.class));
    }
    @Test
    void testAddNewUser_Failure_UserAlreadyExists() {
        String username = "existinguser";
        String password = "password123";
        Role role = Role.USER;
        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(new User()));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.addNewUser(username, password, role);
        });
        assertEquals("User already exist", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
    // READ
    @Test
    void testGetUserById_UserExists() {
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setUsername("testuser");
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
        User result = userService.getUserById(1);
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("testuser", result.getUsername());
    }
    @Test
    void testGetUserById_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        User result = userService.getUserById(1);
        assertNull(result);
    }
    @Test
    void testGetUsers() {
        List<User> mockUsers = Arrays.asList(new User(), new User());
        when(userRepository.findAll()).thenReturn(mockUsers);
        List<User> result = userService.getUsers();
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }
    // UPDATE
    @Test
    void testUpdatePassword_UserNotFound() {
        String username = "testuser";
        String oldPassword = "oldpass";
        String newPassword = "newpass";
        when(userRepository.findUserByUsername(username)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.updatePassword(username, oldPassword, newPassword));
    }
    @Test
    void testUpdatePassword_OldPasswordDoesNotMatch() {
        String username = "testuser";
        String oldPassword = "oldpass";
        String newPassword = "newpass";
        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword("encodedOldPassword");
        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(oldPassword, mockUser.getPassword())).thenReturn(false);
        assertThrows(RuntimeException.class, () -> userService.updatePassword(username, oldPassword, newPassword));
    }
    @Test
    void testUpdatePassword_Success() {
        String username = "testuser";
        String oldPassword = "oldpass";
        String newPassword = "newpass";
        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword("encodedOldPassword");
        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(oldPassword, mockUser.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");
        userService.updatePassword(username, oldPassword, newPassword);
        assertEquals("encodedNewPassword", mockUser.getPassword());
    }
    // DELETE
    @Test
    void testDeleteUser() {
        int userId = 1;
        doNothing().when(userRepository).deleteById(userId);
        userService.deleteUser(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }
}
