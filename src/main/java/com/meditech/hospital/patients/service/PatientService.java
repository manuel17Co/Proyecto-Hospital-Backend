package com.meditech.hospital.patients.service;

import com.meditech.hospital.patients.entity.Patient;
import com.meditech.hospital.patients.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service

public class PatientService {
    private final PatientRepository patientRepository;
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }
    public Patient createPatient(Patient patient) {
        patient.setEstado(Patient.Estado.ACTIVO);
        return patientRepository.save(patient);
    }
    public List<Patient> getAllPatients() {
        return patientRepository.findByEstado(Patient.Estado.ACTIVO);
    }
    public List<Patient> getPatientsByEstado(Patient.Estado estado) {
        return patientRepository.findByEstado(estado);
    }
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
    }
    public Patient updatePatient(Long id, Patient updatePatient) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        patient.setNombre(updatePatient.getNombre());
        patient.setApellido(updatePatient.getApellido());
        patient.setDocumento(updatePatient.getDocumento());
        patient.setTelefono(updatePatient.getTelefono());
        return patientRepository.save(patient);

    }
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        patient.setEstado(Patient.Estado.INACTIVO);
        patientRepository.save(patient);

    }
    public Patient changeStatus(Long id, Patient.Estado estado) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        patient.setEstado(estado);
        return patientRepository.save(patient);
    }
}