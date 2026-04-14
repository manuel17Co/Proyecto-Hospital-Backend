package com.meditech.hospital.doctors.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meditech.hospital.doctors.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {}
