package com.dev.user;

import com.dev.auth.Role;
import com.dev.auth.User;
import com.dev.auth.UserRepository;
import com.dev.auth.UserService;
import com.dev.auth.controller.UpdatePasswordDto;
import com.dev.auth.controller.UserRegistrationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Create a new test user for each test
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setRole(Role.USER);
        userRepository.save(testUser);
    }

    @AfterEach
    void cleanUp() {
        userRepository.delete(testUser);
    }

    @Test
    @WithMockUser
    void testPostUser() throws Exception {
        UserRegistrationDTO request = new UserRegistrationDTO();
        request.setUsername("newuser");
        request.setPassword("password123");
        request.setRole(Role.USER);
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
        User createdUser = userRepository.findUserByUsername("newuser").orElse(null);
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getUsername()).isEqualTo("newuser");
    }

    @Test
    @WithMockUser
    void testGetUsers() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value(testUser.getUsername()));
    }

    @Test
    @WithMockUser
    void testGetUserById() throws Exception {
        mockMvc.perform(get("/api/v1/users/{id}", testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(testUser.getId()))
                .andExpect(jsonPath("$.username").value(testUser.getUsername()));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testUpdatePassword() throws Exception {
        UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto();
        updatePasswordDto.setOldPassword("password123");
        updatePasswordDto.setNewPassword("newPassword");
        mockMvc.perform(put("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePasswordDto)))
                .andExpect(status().isOk());
        User updatedUser = userRepository.findUserByUsername("testuser").orElse(null);
        assertThat(updatedUser).isNotNull();
        assertThat(passwordEncoder.matches("newPassword", updatedUser.getPassword())).isTrue();
    }

    @Test
    @WithMockUser
    void testDeleteUserById() throws Exception {
        mockMvc.perform(delete("/api/v1/users/{id}", testUser.getId()))
                .andExpect(status().isOk());
        User deletedUser = userRepository.findUserByUsername(testUser.getUsername()).orElse(null);
        assertThat(deletedUser).isNull();
    }
}
