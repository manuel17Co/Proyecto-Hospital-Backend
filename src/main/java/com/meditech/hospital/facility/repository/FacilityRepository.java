package com.meditech.hospital.facility.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meditech.hospital.facility.entity.Facility;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    List<Facility> findByTipo(String tipo);
}