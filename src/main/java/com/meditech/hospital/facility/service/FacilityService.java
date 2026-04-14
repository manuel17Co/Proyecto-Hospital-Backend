package com.meditech.hospital.facility.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.meditech.hospital.facility.dto.CreateFacilityDto;
import com.meditech.hospital.facility.entity.Facility;
import com.meditech.hospital.facility.repository.FacilityRepository;

@Service
public class FacilityService {

    private final FacilityRepository facilityRepository;

    public FacilityService(FacilityRepository facilityRepository) {
        this.facilityRepository = facilityRepository;
    }

    public List<Facility> obtenerTodas(String tipo) {
        if (tipo != null && !tipo.isEmpty()) {
            return facilityRepository.findByTipo(tipo);
        }
        return facilityRepository.findAll();
    }

    public Facility obtenerPorId(Long id) {
        return facilityRepository.findById(id).orElseThrow(() -> new RuntimeException("Instalación no encontrada"));
    }

    public Facility guardar(CreateFacilityDto dto) {
        Facility facility = new Facility();
        facility.setNombre(dto.getNombre());
        facility.setTipo(dto.getTipo());
        facility.setUbicacion(dto.getUbicacion());
        return facilityRepository.save(facility);
    }

    public Facility actualizar(Long id, CreateFacilityDto dto) {
        Facility facility = obtenerPorId(id);
        facility.setNombre(dto.getNombre());
        facility.setTipo(dto.getTipo());
        facility.setUbicacion(dto.getUbicacion());
        return facilityRepository.save(facility);
    }

    public void eliminar(Long id) {
        facilityRepository.deleteById(id);
    }
}