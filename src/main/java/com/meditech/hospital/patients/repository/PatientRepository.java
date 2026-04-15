package com.meditech.hospital.patients.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meditech.hospital.patients.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByEstado(Patient.Estado estado);
}
