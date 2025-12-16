package com.medicare.frontend.controller;

import com.medicare.frontend.client.HmsBackendClient;
import com.medicare.frontend.dto.DepartmentDto;
import com.medicare.frontend.dto.NurseSummaryDto;
import com.medicare.frontend.dto.PatientDto;
import com.medicare.frontend.dto.PhysicianDto;
import com.medicare.frontend.dto.PrescribesDto;
import com.medicare.frontend.dto.ProcedureDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class PageController {

    private final HmsBackendClient backendClient;

    public PageController(HmsBackendClient backendClient) {
        this.backendClient = backendClient;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageRoute", "home");
        return "index";
    }

    @GetMapping("/team/akhila")
    public String akhila(@RequestParam(value = "physicianId", required = false) Integer physicianId,
                         Model model) {
        List<PhysicianDto> physicians = backendClient.fetchPhysicians();
        Integer selectedPhysicianId = determineSelection(physicianId, physicians.stream()
                .map(PhysicianDto::getEmployeeId)
                .filter(id -> id != null)
                .sorted()
                .toList());

        List<PatientDto> patients = selectedPhysicianId == null
                ? List.of()
                : backendClient.fetchPatientsByPhysician(selectedPhysicianId);

        Optional<PhysicianDto> selectedPhysician = physicians.stream()
                .filter(p -> p.getEmployeeId() != null && p.getEmployeeId().equals(selectedPhysicianId))
                .findFirst();

        model.addAttribute("pageRoute", "akhila");
        model.addAttribute("physicians", physicians);
        model.addAttribute("selectedPhysicianId", selectedPhysicianId);
        model.addAttribute("selectedPhysician", selectedPhysician.orElse(null));
        model.addAttribute("patients", patients);
        return "team/akhila";
    }

    @GetMapping("/team/karthik")
    public String karthik(@RequestParam(value = "procedureCode", required = false) Integer procedureCode,
                          Model model) {
        List<ProcedureDto> procedures = backendClient.fetchProcedures();
        Integer selectedCode = determineSelection(procedureCode, procedures.stream()
                .map(ProcedureDto::getCode)
                .sorted()
                .toList());

        List<PhysicianDto> trainedPhysicians = selectedCode == null
                ? List.of()
                : backendClient.fetchPhysiciansByProcedure(selectedCode);

        Optional<ProcedureDto> selectedProcedure = procedures.stream()
                .filter(proc -> proc.getCode() != null && proc.getCode().equals(selectedCode))
                .findFirst();

        model.addAttribute("pageRoute", "karthik");
        model.addAttribute("procedures", procedures);
        model.addAttribute("selectedProcedure", selectedProcedure.orElse(null));
        model.addAttribute("selectedProcedureCode", selectedCode);
        model.addAttribute("trainedPhysicians", trainedPhysicians);
        return "team/karthik";
    }

    @GetMapping("/team/keerthana")
    public String keerthana(@RequestParam(value = "patientId", required = false) Integer patientId,
                            Model model) {
        List<PatientDto> patients = backendClient.fetchPatients();
        List<Integer> ssns = patients.stream()
                .map(PatientDto::getSsn)
                .filter(id -> id != null)
                .sorted()
                .toList();
        Integer selectedPatientId = determineSelection(patientId, ssns);

        List<String> appointmentDates = selectedPatientId == null
                ? List.of()
                : backendClient.fetchAppointmentDates(selectedPatientId);

        Optional<PatientDto> selectedPatient = patients.stream()
                .filter(p -> p.getSsn() != null && p.getSsn().equals(selectedPatientId))
                .findFirst();

        model.addAttribute("pageRoute", "keerthana");
        model.addAttribute("patients", patients);
        model.addAttribute("selectedPatient", selectedPatient.orElse(null));
        model.addAttribute("selectedPatientId", selectedPatientId);
        model.addAttribute("appointmentDates", appointmentDates);
        return "team/keerthana";
    }

    @GetMapping("/team/mahitha")
    public String mahitha(@RequestParam(value = "nurseId", required = false) Integer nurseId,
                          Model model) {
        List<NurseSummaryDto> nurses = backendClient.fetchNurses();
        List<Integer> nurseIds = nurses.stream()
                .map(NurseSummaryDto::getEmployeeId)
                .filter(id -> id != null)
                .sorted()
                .toList();
        Integer selectedNurseId = determineSelection(nurseId, nurseIds);

        List<PatientDto> nursePatients = selectedNurseId == null
                ? List.of()
                : backendClient.fetchPatientsByNurse(selectedNurseId);

        Optional<NurseSummaryDto> selectedNurse = nurses.stream()
                .filter(n -> n.getEmployeeId() != null && n.getEmployeeId().equals(selectedNurseId))
                .findFirst();

        model.addAttribute("pageRoute", "mahitha");
        model.addAttribute("nurses", nurses);
        model.addAttribute("selectedNurse", selectedNurse.orElse(null));
        model.addAttribute("selectedNurseId", selectedNurseId);
        model.addAttribute("nursePatients", nursePatients);
        return "team/mahitha";
    }

    @GetMapping("/team/kanishka")
    public String kanishka(@RequestParam(value = "ssn", required = false) Integer ssn,
                           Model model) {
        List<PatientDto> patients = backendClient.fetchPatients();
        List<Integer> allSsns = patients.stream()
                .map(PatientDto::getSsn)
                .filter(id -> id != null)
                .sorted()
                .toList();
        Integer selectedSsn = determineSelection(ssn, allSsns);

        List<PrescribesDto> prescriptions = selectedSsn == null
                ? List.of()
                : backendClient.fetchPrescriptionsByPatient(selectedSsn);

        Optional<PatientDto> selectedPatient = patients.stream()
                .filter(p -> p.getSsn() != null && p.getSsn().equals(selectedSsn))
                .findFirst();

        model.addAttribute("pageRoute", "kanishka");
        model.addAttribute("patients", patients);
        model.addAttribute("selectedPatient", selectedPatient.orElse(null));
        model.addAttribute("selectedSsn", selectedSsn);
        model.addAttribute("prescriptions", prescriptions);
        return "team/kanishka";
    }

    @GetMapping("/team/samiksha")
    public String samiksha(@RequestParam(value = "physicianId", required = false) Integer physicianId,
                           Model model) {
        List<PhysicianDto> physicians = backendClient.fetchPhysicians();
        List<Integer> ids = physicians.stream()
                .map(PhysicianDto::getEmployeeId)
                .filter(id -> id != null)
                .sorted()
                .toList();
        Integer selectedId = determineSelection(physicianId, ids);

        List<DepartmentDto> departments = selectedId == null
                ? List.of()
                : backendClient.fetchDepartmentsForPhysician(selectedId);

        Optional<PhysicianDto> selectedPhysician = physicians.stream()
                .filter(p -> p.getEmployeeId() != null && p.getEmployeeId().equals(selectedId))
                .findFirst();

        model.addAttribute("pageRoute", "samiksha");
        model.addAttribute("physicians", physicians);
        model.addAttribute("selectedPhysician", selectedPhysician.orElse(null));
        model.addAttribute("selectedPhysicianId", selectedId);
        model.addAttribute("departments", departments);
        return "team/samiksha";
    }

    private Integer determineSelection(Integer requestedId, List<Integer> availableIds) {
        if (availableIds.isEmpty()) {
            return null;
        }
        if (requestedId != null && availableIds.contains(requestedId)) {
            return requestedId;
        }
        return availableIds.get(0);
    }
}
