package com.meditech.hospital.facility.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meditech.hospital.facility.entity.Facility;

public interface FacilityRepository extends JpaRepository<Facility, Long> {}
