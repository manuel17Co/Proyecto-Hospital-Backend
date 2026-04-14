package com.meditech.hospital.patients.controller;
import com.meditech.hospital.patients.dto.CreatePatientDto;
import com.meditech.hospital.patients.dto.GetPatientDto;
import com.meditech.hospital.patients.entity.Patient;
import com.meditech.hospital.patients.service.PatientService;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }
    @PostMapping
    public GetPatientDto createPatient(@RequestBody CreatePatientDto createPatientDto) {
        Patient patient = new Patient();
        patient.setNombre(createPatientDto.getNombre());
        patient.setApellido(createPatientDto.getApellido());
        patient.setDocumento(createPatientDto.getDocumento());
        patient.setTelefono(createPatientDto.getTelefono());
        Patient saved = patientService.createPatient(patient);
        return mapToDto(saved);
    }
    @GetMapping
    public List<GetPatientDto> getAll(@RequestParam(required = false) String estado) {
        List<Patient> patients;

        if(estado!= null) {
            patients = patientService.getPatientsByEstado(Patient.Estado.valueOf(estado));
        }else {
            patients = patientService.getAllPatients();

        }
        return patients.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        }
        @GetMapping("/{id}")
        public GetPatientDto getById(@PathVariable Long id) {
        return mapToDto(patientService.getPatientById(id));
    }
@PutMapping("/{id}")
    public GetPatientDto update(@PathVariable Long id, @RequestBody CreatePatientDto dto) {

        Patient patient = new Patient();
        patient.setNombre(dto.getNombre());
        patient.setApellido(dto.getApellido());
        patient.setDocumento(dto.getDocumento());
        patient.setTelefono(dto.getTelefono());

        return mapToDto(patientService.updatePatient(id, patient));
    }

    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        patientService.deletePatient(id);
    }

    
    @PatchMapping("/{id}/status")
    public GetPatientDto changeStatus(@PathVariable Long id, @RequestParam String estado) {
        return mapToDto(
                patientService.changeStatus(id, Patient.Estado.valueOf(estado))
        );
    }

    
    private GetPatientDto mapToDto(Patient patient) {
        GetPatientDto dto = new GetPatientDto();

        dto.setId(patient.getId());
        dto.setNombre(patient.getNombre());
        dto.setApellido(patient.getApellido());
        dto.setDocumento(patient.getDocumento());
        dto.setTelefono(patient.getTelefono());
        dto.setEstado(patient.getEstado().name());

        return dto;
    }
}
    

