package pro.dev.TGBotForShelter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pro.dev.TGBotForShelter.model.Shelter;
import pro.dev.TGBotForShelter.model.ShelterType;
import pro.dev.TGBotForShelter.service.ShelterService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тесты для ShelterController
 */
@WebMvcTest(ShelterController.class)
class ShelterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShelterService shelterService;

    @Autowired
    private ObjectMapper objectMapper;

    private Shelter testShelter;

    @BeforeEach
    void setUp() {
        testShelter = new Shelter();
        testShelter.setId(1L);
        testShelter.setType(ShelterType.CAT);
        testShelter.setName("Приют для кошек");
        testShelter.setAddress("Москва, ул. Примерная, 1");
        testShelter.setSchedule("Пн-Пт: 9:00-18:00");
        testShelter.setSecurityContact("+7 495 123-45-67");
        testShelter.setSafetyRules("Не кормить животных");
        testShelter.setInformation("Приют работает с 2010 года");
    }

    @Test
    void createShelter_ReturnsCreated() throws Exception {
        when(shelterService.createShelter(any(Shelter.class))).thenReturn(1L);

        mockMvc.perform(post("/api/shelters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testShelter)))
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));

        verify(shelterService, times(1)).createShelter(any(Shelter.class));
    }

    @Test
    void getShelter_ReturnsOk() throws Exception {
        when(shelterService.getShelter(1L)).thenReturn(testShelter);

        mockMvc.perform(get("/api/shelters/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Приют для кошек"))
                .andExpect(jsonPath("$.type").value("CAT"));

        verify(shelterService, times(1)).getShelter(1L);
    }

    @Test
    void getAllShelters_ReturnsOk() throws Exception {
        List<Shelter> shelters = Arrays.asList(testShelter);
        when(shelterService.getAllShelters(null)).thenReturn(shelters);

        mockMvc.perform(get("/api/shelters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Приют для кошек"));

        verify(shelterService, times(1)).getAllShelters(null);
    }

    @Test
    void getAllShelters_WithTypeFilter_ReturnsOk() throws Exception {
        List<Shelter> shelters = Arrays.asList(testShelter);
        when(shelterService.getAllShelters(ShelterType.CAT)).thenReturn(shelters);

        mockMvc.perform(get("/api/shelters?type=CAT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("CAT"));

        verify(shelterService, times(1)).getAllShelters(ShelterType.CAT);
    }

    @Test
    void updateShelter_ReturnsOk() throws Exception {
        when(shelterService.updateShelter(eq(1L), any(Shelter.class))).thenReturn(testShelter);

        mockMvc.perform(put("/api/shelters/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testShelter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(shelterService, times(1)).updateShelter(eq(1L), any(Shelter.class));
    }

    @Test
    void deleteShelter_ReturnsNoContent() throws Exception {
        doNothing().when(shelterService).deleteShelter(1L);

        mockMvc.perform(delete("/api/shelters/1"))
                .andExpect(status().isNoContent());

        verify(shelterService, times(1)).deleteShelter(1L);
    }
}
