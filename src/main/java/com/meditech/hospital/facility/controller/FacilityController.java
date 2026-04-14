package com.meditech.hospital.facility.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.meditech.hospital.facility.dto.CreateFacilityDto;
import com.meditech.hospital.facility.entity.Facility;
import com.meditech.hospital.facility.service.FacilityService;

@RestController
@RequestMapping("/api/v1/facilities")
public class FacilityController {

    private final FacilityService facilityService;

    public FacilityController(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    @GetMapping
    public ResponseEntity<List<Facility>> listarInstalaciones(@RequestParam(required = false) String tipo) {
        return ResponseEntity.ok(facilityService.obtenerTodas(tipo));
    }

    @PostMapping
    public ResponseEntity<Facility> crearInstalacion(@RequestBody CreateFacilityDto createFacilityDto) {
        return new ResponseEntity<>(facilityService.guardar(createFacilityDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Facility> obtenerInstalacion(@PathVariable Long id) {
        return ResponseEntity.ok(facilityService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Facility> actualizarInstalacion(@PathVariable Long id, @RequestBody CreateFacilityDto createFacilityDto) {
        return ResponseEntity.ok(facilityService.actualizar(id, createFacilityDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInstalacion(@PathVariable Long id) {
        facilityService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}