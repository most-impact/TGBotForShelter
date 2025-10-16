package pro.dev.TGBotForShelter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pro.dev.TGBotForShelter.model.DailyReport;
import pro.dev.TGBotForShelter.model.TrialPeriod;
import pro.dev.TGBotForShelter.service.DailyReportService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тесты для DailyReportController
 */
@WebMvcTest(DailyReportController.class)
class DailyReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DailyReportService dailyReportService;

    @Autowired
    private ObjectMapper objectMapper;

    private DailyReport testReport;

    @BeforeEach
    void setUp() {
        TrialPeriod trialPeriod = new TrialPeriod();
        trialPeriod.setId(1L);

        testReport = new DailyReport();
        testReport.setId(1L);
        testReport.setTrialPeriod(trialPeriod);
        testReport.setReportDate(LocalDate.now());
        testReport.setPhotoPath("/photos/cat1.jpg");
        testReport.setDiet("Royal Canin");
        testReport.setWellBeing("Отлично");
        testReport.setBehaviorChanges("Активный");
        testReport.setCreatedAt(LocalDateTime.now());
        testReport.setReviewed(false);
        testReport.setPoorQuality(false);
    }

    @Test
    void createDailyReport_ReturnsCreated() throws Exception {
        when(dailyReportService.createDailyReport(any(DailyReport.class))).thenReturn(1L);

        mockMvc.perform(post("/api/daily-reports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testReport)))
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));

        verify(dailyReportService, times(1)).createDailyReport(any(DailyReport.class));
    }

    @Test
    void getDailyReport_ReturnsOk() throws Exception {
        when(dailyReportService.getDailyReport(1L)).thenReturn(testReport);

        mockMvc.perform(get("/api/daily-reports/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.diet").value("Royal Canin"));

        verify(dailyReportService, times(1)).getDailyReport(1L);
    }

    @Test
    void getAllDailyReports_ReturnsOk() throws Exception {
        List<DailyReport> reports = Arrays.asList(testReport);
        when(dailyReportService.getAllDailyReports(null, null, null)).thenReturn(reports);

        mockMvc.perform(get("/api/daily-reports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(dailyReportService, times(1)).getAllDailyReports(null, null, null);
    }

    @Test
    void getReportsForVolunteerReview_ReturnsOk() throws Exception {
        List<DailyReport> reports = Arrays.asList(testReport);
        when(dailyReportService.getReportsForVolunteerReview()).thenReturn(reports);

        mockMvc.perform(get("/api/daily-reports/for-review"))
                .andExpect(status().isOk());

        verify(dailyReportService, times(1)).getReportsForVolunteerReview();
    }

    @Test
    void hasTodayReport_ReturnsBoolean() throws Exception {
        when(dailyReportService.hasTodayReport(1L)).thenReturn(true);

        mockMvc.perform(get("/api/daily-reports/has-today/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(dailyReportService, times(1)).hasTodayReport(1L);
    }

    @Test
    void reviewReport_ReturnsOk() throws Exception {
        testReport.setReviewed(true);
        when(dailyReportService.reviewReport(1L, false)).thenReturn(testReport);

        mockMvc.perform(patch("/api/daily-reports/1/review?poorQuality=false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewed").value(true));

        verify(dailyReportService, times(1)).reviewReport(1L, false);
    }

    @Test
    void updateDailyReport_ReturnsOk() throws Exception {
        when(dailyReportService.updateDailyReport(eq(1L), any(DailyReport.class))).thenReturn(testReport);

        mockMvc.perform(put("/api/daily-reports/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testReport)))
                .andExpect(status().isOk());

        verify(dailyReportService, times(1)).updateDailyReport(eq(1L), any(DailyReport.class));
    }

    @Test
    void deleteDailyReport_ReturnsNoContent() throws Exception {
        doNothing().when(dailyReportService).deleteDailyReport(1L);

        mockMvc.perform(delete("/api/daily-reports/1"))
                .andExpect(status().isNoContent());

        verify(dailyReportService, times(1)).deleteDailyReport(1L);
    }
}
