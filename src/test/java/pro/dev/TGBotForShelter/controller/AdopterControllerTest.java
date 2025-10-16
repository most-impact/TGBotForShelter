package pro.dev.TGBotForShelter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pro.dev.TGBotForShelter.model.Adopter;
import pro.dev.TGBotForShelter.model.Pet;
import pro.dev.TGBotForShelter.model.Shelter;
import pro.dev.TGBotForShelter.model.User;
import pro.dev.TGBotForShelter.service.AdopterService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тесты для AdopterController
 */
@WebMvcTest(AdopterController.class)
class AdopterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdopterService adopterService;

    @Autowired
    private ObjectMapper objectMapper;

    private Adopter testAdopter;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(1L);

        Shelter shelter = new Shelter();
        shelter.setId(1L);

        Pet pet = new Pet();
        pet.setId(1L);

        testAdopter = new Adopter();
        testAdopter.setId(1L);
        testAdopter.setUser(user);
        testAdopter.setShelter(shelter);
        testAdopter.setPet(pet);
        testAdopter.setAdoptionDate(LocalDateTime.now());
    }

    @Test
    void createAdopter_ReturnsCreated() throws Exception {
        when(adopterService.createAdopter(any(Adopter.class))).thenReturn(1L);

        mockMvc.perform(post("/api/adopters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAdopter)))
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));

        verify(adopterService, times(1)).createAdopter(any(Adopter.class));
    }

    @Test
    void getAdopter_ReturnsOk() throws Exception {
        when(adopterService.getAdopter(1L)).thenReturn(testAdopter);

        mockMvc.perform(get("/api/adopters/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(adopterService, times(1)).getAdopter(1L);
    }

    @Test
    void getAdopterByUserId_ReturnsOk() throws Exception {
        when(adopterService.getAdopterByUserId(1L)).thenReturn(testAdopter);

        mockMvc.perform(get("/api/adopters/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id").value(1));

        verify(adopterService, times(1)).getAdopterByUserId(1L);
    }

    @Test
    void getAllAdopters_ReturnsOk() throws Exception {
        List<Adopter> adopters = Arrays.asList(testAdopter);
        when(adopterService.getAllAdopters(null)).thenReturn(adopters);

        mockMvc.perform(get("/api/adopters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(adopterService, times(1)).getAllAdopters(null);
    }

    @Test
    void getRecentAdopters_ReturnsOk() throws Exception {
        List<Adopter> adopters = Arrays.asList(testAdopter);
        when(adopterService.getRecentAdopters(30)).thenReturn(adopters);

        mockMvc.perform(get("/api/adopters/recent"))
                .andExpect(status().isOk());

        verify(adopterService, times(1)).getRecentAdopters(30);
    }

    @Test
    void updateAdopter_ReturnsOk() throws Exception {
        when(adopterService.updateAdopter(eq(1L), any(Adopter.class))).thenReturn(testAdopter);

        mockMvc.perform(put("/api/adopters/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAdopter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(adopterService, times(1)).updateAdopter(eq(1L), any(Adopter.class));
    }

    @Test
    void deleteAdopter_ReturnsNoContent() throws Exception {
        doNothing().when(adopterService).deleteAdopter(1L);

        mockMvc.perform(delete("/api/adopters/1"))
                .andExpect(status().isNoContent());

        verify(adopterService, times(1)).deleteAdopter(1L);
    }
}
