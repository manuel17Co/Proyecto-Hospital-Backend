package com.meditech.hospital.patients.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meditech.hospital.patients.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {}
