package com.meditech.hospital.patients.repository;
import com.meditech.hospital.patients.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByEstado(Patient.estado estado);
}


