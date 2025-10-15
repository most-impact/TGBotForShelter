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
import pro.dev.TGBotForShelter.model.TrialPeriod;
import pro.dev.TGBotForShelter.model.TrialPeriodStatus;
import pro.dev.TGBotForShelter.service.TrialPeriodService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тесты для TrialPeriodController
 */
@WebMvcTest(TrialPeriodController.class)
class TrialPeriodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrialPeriodService trialPeriodService;

    @Autowired
    private ObjectMapper objectMapper;

    private TrialPeriod testTrialPeriod;

    @BeforeEach
    void setUp() {
        Adopter adopter = new Adopter();
        adopter.setId(1L);

        testTrialPeriod = new TrialPeriod();
        testTrialPeriod.setId(1L);
        testTrialPeriod.setAdopter(adopter);
        testTrialPeriod.setStartDate(LocalDate.now());
        testTrialPeriod.setEndDate(LocalDate.now().plusDays(30));
        testTrialPeriod.setStatus(TrialPeriodStatus.ACTIVE);
        testTrialPeriod.setMissedReports(0);
    }

    @Test
    void createTrialPeriod_ReturnsCreated() throws Exception {
        when(trialPeriodService.createTrialPeriod(any(TrialPeriod.class))).thenReturn(1L);

        mockMvc.perform(post("/api/trial-periods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTrialPeriod)))
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));

        verify(trialPeriodService, times(1)).createTrialPeriod(any(TrialPeriod.class));
    }

    @Test
    void getTrialPeriod_ReturnsOk() throws Exception {
        when(trialPeriodService.getTrialPeriod(1L)).thenReturn(testTrialPeriod);

        mockMvc.perform(get("/api/trial-periods/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        verify(trialPeriodService, times(1)).getTrialPeriod(1L);
    }

    @Test
    void getTrialPeriodByAdopterId_ReturnsOk() throws Exception {
        when(trialPeriodService.getTrialPeriodByAdopterId(1L)).thenReturn(testTrialPeriod);

        mockMvc.perform(get("/api/trial-periods/adopter/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.adopter.id").value(1));

        verify(trialPeriodService, times(1)).getTrialPeriodByAdopterId(1L);
    }

    @Test
    void getAllTrialPeriods_ReturnsOk() throws Exception {
        List<TrialPeriod> trialPeriods = Arrays.asList(testTrialPeriod);
        when(trialPeriodService.getAllTrialPeriods(null, null)).thenReturn(trialPeriods);

        mockMvc.perform(get("/api/trial-periods"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(trialPeriodService, times(1)).getAllTrialPeriods(null, null);
    }

    @Test
    void getActiveTrialPeriods_ReturnsOk() throws Exception {
        List<TrialPeriod> trialPeriods = Arrays.asList(testTrialPeriod);
        when(trialPeriodService.getActiveTrialPeriods()).thenReturn(trialPeriods);

        mockMvc.perform(get("/api/trial-periods/active"))
                .andExpect(status().isOk());

        verify(trialPeriodService, times(1)).getActiveTrialPeriods();
    }

    @Test
    void passTrialPeriod_ReturnsOk() throws Exception {
        testTrialPeriod.setStatus(TrialPeriodStatus.PASSED);
        when(trialPeriodService.passTrialPeriod(1L)).thenReturn(testTrialPeriod);

        mockMvc.perform(patch("/api/trial-periods/1/pass"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PASSED"));

        verify(trialPeriodService, times(1)).passTrialPeriod(1L);
    }

    @Test
    void extendTrialPeriod14Days_ReturnsOk() throws Exception {
        when(trialPeriodService.extendTrialPeriod14Days(1L)).thenReturn(testTrialPeriod);

        mockMvc.perform(patch("/api/trial-periods/1/extend-14"))
                .andExpect(status().isOk());

        verify(trialPeriodService, times(1)).extendTrialPeriod14Days(1L);
    }

    @Test
    void updateTrialPeriod_ReturnsOk() throws Exception {
        when(trialPeriodService.updateTrialPeriod(eq(1L), any(TrialPeriod.class))).thenReturn(testTrialPeriod);

        mockMvc.perform(put("/api/trial-periods/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTrialPeriod)))
                .andExpect(status().isOk());

        verify(trialPeriodService, times(1)).updateTrialPeriod(eq(1L), any(TrialPeriod.class));
    }

    @Test
    void deleteTrialPeriod_ReturnsNoContent() throws Exception {
        doNothing().when(trialPeriodService).deleteTrialPeriod(1L);

        mockMvc.perform(delete("/api/trial-periods/1"))
                .andExpect(status().isNoContent());

        verify(trialPeriodService, times(1)).deleteTrialPeriod(1L);
    }
}
