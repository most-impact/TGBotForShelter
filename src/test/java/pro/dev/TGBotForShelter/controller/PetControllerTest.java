package pro.dev.TGBotForShelter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pro.dev.TGBotForShelter.model.Pet;
import pro.dev.TGBotForShelter.model.ShelterType;
import pro.dev.TGBotForShelter.service.PetService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тесты для PetController
 */
@WebMvcTest(PetController.class)
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetService petService;

    @Autowired
    private ObjectMapper objectMapper;

    private Pet testPet;

    @BeforeEach
    void setUp() {
        testPet = new Pet();
        testPet.setId(1L);
        testPet.setName("Мурзик");
        testPet.setType(ShelterType.CAT);
        testPet.setBreed("Британская");
        testPet.setAge(2);
        testPet.setDescription("Ласковый кот");
        testPet.setHasDisabilities(false);
    }

    @Test
    void createPet_ReturnsCreated() throws Exception {
        when(petService.createPet(any(Pet.class))).thenReturn(1L);

        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPet)))
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));

        verify(petService, times(1)).createPet(any(Pet.class));
    }

    @Test
    void getPet_ReturnsOk() throws Exception {
        when(petService.getPet(1L)).thenReturn(testPet);

        mockMvc.perform(get("/api/pets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Мурзик"))
                .andExpect(jsonPath("$.type").value("CAT"));

        verify(petService, times(1)).getPet(1L);
    }

    @Test
    void getAllPets_ReturnsOk() throws Exception {
        List<Pet> pets = Arrays.asList(testPet);
        when(petService.getAllPets(null, null)).thenReturn(pets);

        mockMvc.perform(get("/api/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(petService, times(1)).getAllPets(null, null);
    }

    @Test
    void getAllPets_WithFilters_ReturnsOk() throws Exception {
        List<Pet> pets = Arrays.asList(testPet);
        when(petService.getAllPets(ShelterType.CAT, false)).thenReturn(pets);

        mockMvc.perform(get("/api/pets?type=CAT&hasDisabilities=false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("CAT"));

        verify(petService, times(1)).getAllPets(ShelterType.CAT, false);
    }

    @Test
    void searchPetsByName_ReturnsOk() throws Exception {
        List<Pet> pets = Arrays.asList(testPet);
        when(petService.searchPetsByName("Мурзик")).thenReturn(pets);

        mockMvc.perform(get("/api/pets/search?name=Мурзик"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Мурзик"));

        verify(petService, times(1)).searchPetsByName("Мурзик");
    }

    @Test
    void getYoungPets_ReturnsOk() throws Exception {
        List<Pet> pets = Arrays.asList(testPet);
        when(petService.getYoungPets(ShelterType.CAT, 3)).thenReturn(pets);

        mockMvc.perform(get("/api/pets/young?type=CAT&maxAge=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].age").value(2));

        verify(petService, times(1)).getYoungPets(ShelterType.CAT, 3);
    }

    @Test
    void updatePet_ReturnsOk() throws Exception {
        when(petService.updatePet(eq(1L), any(Pet.class))).thenReturn(testPet);

        mockMvc.perform(put("/api/pets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(petService, times(1)).updatePet(eq(1L), any(Pet.class));
    }

    @Test
    void deletePet_ReturnsNoContent() throws Exception {
        doNothing().when(petService).deletePet(1L);

        mockMvc.perform(delete("/api/pets/1"))
                .andExpect(status().isNoContent());

        verify(petService, times(1)).deletePet(1L);
    }
}

