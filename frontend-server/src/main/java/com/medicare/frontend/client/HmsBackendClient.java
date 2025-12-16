package com.medicare.frontend.client;

import com.medicare.frontend.dto.DepartmentDto;
import com.medicare.frontend.dto.NurseSummaryDto;
import com.medicare.frontend.dto.PatientDto;
import com.medicare.frontend.dto.PhysicianDto;
import com.medicare.frontend.dto.PrescribesDto;
import com.medicare.frontend.dto.ProcedureDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class HmsBackendClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public HmsBackendClient(RestTemplate restTemplate,
                            @Value("${hmsbackend.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    public List<PhysicianDto> fetchPhysicians() {
        return getList("/physician", PhysicianDto[].class);
    }

    public List<PatientDto> fetchPatients() {
        return getList("/patient", PatientDto[].class);
    }

    public List<PatientDto> fetchPatientsByPhysician(Integer physicianId) {
        return getList("/patient/" + physicianId, PatientDto[].class);
    }

    public List<ProcedureDto> fetchProcedures() {
        return getList("/procedure", ProcedureDto[].class);
    }

    public List<PhysicianDto> fetchPhysiciansByProcedure(Integer procedureCode) {
        return getList("/trained_in/physicians/" + procedureCode, PhysicianDto[].class);
    }

    public List<String> fetchAppointmentDates(Integer patientId) {
        return getList("/appointment/date/" + patientId, String[].class);
    }

    public List<NurseSummaryDto> fetchNurses() {
        return getList("/nurse", NurseSummaryDto[].class);
    }

    public List<PatientDto> fetchPatientsByNurse(Integer nurseId) {
        return getList("/patient/patient/" + nurseId, PatientDto[].class);
    }

    public List<PrescribesDto> fetchPrescriptionsByPatient(Integer ssn) {
        return getList("/prescribes/patient/" + ssn, PrescribesDto[].class);
    }

    public List<DepartmentDto> fetchDepartmentsForPhysician(Integer physicianId) {
        return getList("/affiliated_with/department/" + physicianId, DepartmentDto[].class);
    }

    private <T> List<T> getList(String path, Class<T[]> type) {
        try {
            T[] body = restTemplate.getForObject(url(path), type);
            if (body == null) {
                return List.of();
            }
            return Arrays.asList(body);
        } catch (HttpClientErrorException ex) {
            HttpStatusCode statusCode = ex.getStatusCode();
            int code = statusCode.value();
            if (code == 204 || code == 404) {
                return List.of();
            }
            throw new BackendClientException("Failed to call backend path " + path, ex);
        } catch (RestClientException ex) {
            throw new BackendClientException("Failed to call backend path " + path, ex);
        }
    }

    private String url(String path) {
        if (path.startsWith("http://") || path.startsWith("https://")) {
            return path;
        }
        String normalized = path.startsWith("/") ? path : "/" + path;
        return baseUrl + normalized;
    }
}
