package ru.kvs.doctrspring.adapters.restapi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.kvs.doctrspring.adapters.restapi.dto.response.ClinicDto;
import ru.kvs.doctrspring.adapters.restapi.mapper.ClinicMapper;
import ru.kvs.doctrspring.app.ClinicService;
import ru.kvs.doctrspring.domain.Clinic;
import ru.kvs.doctrspring.domain.ids.ClinicId;
import ru.kvs.doctrspring.security.AuthUtil;
import ru.kvs.doctrspring.security.JwtTokenFilter;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.kvs.doctrspring.domain.Status.ACTIVE;

@WebMvcTest(controllers = ClinicRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ClinicRestControllerTest {

    private static final ClinicId CLINIC_ID = ClinicId.of("333fda40-6b43-4770-912c-81dc6e1ec163");

    private static final String REST_URL = "/api/v1/clinics/";

    @MockBean
    private ClinicService clinicService;
    @MockBean
    private ClinicMapper clinicMapper;
    @MockBean
    JwtTokenFilter jwtTokenFilter;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("getAll should return active clinics for doctor")
    void testGetAll() throws Exception {
        // given
        List<Clinic> expectedClinics = List.of(
                Clinic.builder().id(CLINIC_ID)
                        .name("Clinic1")
                        .phone("123")
                        .address("Address1")
                        .status(ACTIVE)
                        .build());
        List<ClinicDto> expectedDtos = List.of(ClinicDto.builder()
                .id(CLINIC_ID.asString())
                .name("Clinic1")
                .phone("123")
                .address("Address1")
                .status(ACTIVE)
                .build());
        when(clinicService.getAll(AuthUtil.getAuthUserId())).thenReturn(expectedClinics);
        when(clinicMapper.toClinicDtos(expectedClinics)).thenReturn(expectedDtos);

        // when then
        mockMvc.perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(CLINIC_ID.asString()))
                .andExpect(jsonPath("$[0].name").value("Clinic1"))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"))
                .andExpect(jsonPath("$[0].phone").value("123"))
                .andExpect(jsonPath("$[0].address").value("Address1"));

        verify(clinicService).getAll(AuthUtil.getAuthUserId());
        verify(clinicMapper).toClinicDtos(expectedClinics);
    }

    @Test
    @DisplayName("get should return clinic if found in repository")
    void testGetWhenFound() throws Exception {
        // given
        Clinic expectedClinic = Clinic.builder()
                .id(CLINIC_ID)
                .name("Clinic1")
                .status(ACTIVE)
                .phone("123")
                .address("Address1").build();
        ClinicDto expectedDto = ClinicDto.builder()
                .id(CLINIC_ID.asString())
                .name("Clinic1")
                .status(ACTIVE)
                .phone("123")
                .address("Address1").build();
        when(clinicService.get(CLINIC_ID, AuthUtil.getAuthUserId())).thenReturn(expectedClinic);
        when(clinicMapper.toClinicDto(expectedClinic)).thenReturn(expectedDto);

        // when then
        mockMvc.perform(get(REST_URL + CLINIC_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(CLINIC_ID.asString()))
                .andExpect(jsonPath("name").value("Clinic1"))
                .andExpect(jsonPath("status").value("ACTIVE"))
                .andExpect(jsonPath("phone").value("123"))
                .andExpect(jsonPath("address").value("Address1"));

        verify(clinicService).get(CLINIC_ID, AuthUtil.getAuthUserId());
        verify(clinicMapper).toClinicDto(expectedClinic);
    }

}
