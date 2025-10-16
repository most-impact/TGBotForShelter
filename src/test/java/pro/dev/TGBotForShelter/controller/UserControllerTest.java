package pro.dev.TGBotForShelter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pro.dev.TGBotForShelter.model.User;
import pro.dev.TGBotForShelter.service.UserService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тесты для UserController
 */
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setTelegramId(123456789L);
        testUser.setFirstName("Иван");
        testUser.setLastName("Иванов");
        testUser.setUsername("ivanov");
        testUser.setPhoneNumber("+79991234567");
        testUser.setEmail("ivan@example.com");
        testUser.setRegisteredAt(LocalDateTime.now());
        testUser.setVolunteer(false);
    }

    @Test
    void createUser_ReturnsCreated() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(1L);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));

        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void getUser_ReturnsOk() throws Exception {
        when(userService.getUser(1L)).thenReturn(testUser);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Иван"))
                .andExpect(jsonPath("$.telegramId").value(123456789));

        verify(userService, times(1)).getUser(1L);
    }

    @Test
    void getUserByTelegramId_ReturnsOk() throws Exception {
        when(userService.getUserByTelegramId(123456789L)).thenReturn(testUser);

        mockMvc.perform(get("/api/users/telegram/123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.telegramId").value(123456789));

        verify(userService, times(1)).getUserByTelegramId(123456789L);
    }

    @Test
    void getAllUsers_ReturnsOk() throws Exception {
        List<User> users = Arrays.asList(testUser);
        when(userService.getAllUsers(null)).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(userService, times(1)).getAllUsers(null);
    }

    @Test
    void getVolunteers_ReturnsOk() throws Exception {
        testUser.setVolunteer(true);
        List<User> volunteers = Arrays.asList(testUser);
        when(userService.getVolunteers()).thenReturn(volunteers);

        mockMvc.perform(get("/api/users/volunteers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].volunteer").value(true));

        verify(userService, times(1)).getVolunteers();
    }

    @Test
    void updateUser_ReturnsOk() throws Exception {
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(testUser);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(userService, times(1)).updateUser(eq(1L), any(User.class));
    }

    @Test
    void setVolunteerStatus_ReturnsOk() throws Exception {
        testUser.setVolunteer(true);
        when(userService.setVolunteerStatus(1L, true)).thenReturn(testUser);

        mockMvc.perform(patch("/api/users/1/volunteer?isVolunteer=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.volunteer").value(true));

        verify(userService, times(1)).setVolunteerStatus(1L, true);
    }

    @Test
    void deleteUser_ReturnsNoContent() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }
}
