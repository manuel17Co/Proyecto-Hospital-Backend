package com.meditech.hospital.doctors.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.meditech.hospital.appointment.dto.AppointmentResponse;
import com.meditech.hospital.appointment.service.AppointmentService;
import com.meditech.hospital.doctors.dto.CreateDoctorDto;
import com.meditech.hospital.doctors.entity.Doctor;
import com.meditech.hospital.doctors.service.DoctorService;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final AppointmentService appointmentService;

    public DoctorController(DoctorService doctorService, AppointmentService appointmentService) {
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public ResponseEntity<List<Doctor>> listarMedicos() {
        return ResponseEntity.ok(doctorService.obtenerTodos());
    }

    @GetMapping("/{id}/appointments")
    public List<AppointmentResponse> listAppointmentsByDoctor(@PathVariable Long id) {
        return appointmentService.listByDoctorId(id);
    }


    @PostMapping
    public ResponseEntity<Doctor> crearMedico(@RequestBody CreateDoctorDto createDoctorDto) {
        return new ResponseEntity<>(doctorService.guardar(createDoctorDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> obtenerMedico(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> actualizarMedico(@PathVariable Long id, @RequestBody CreateDoctorDto createDoctorDto) {
        return ResponseEntity.ok(doctorService.actualizar(id, createDoctorDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMedico(@PathVariable Long id) {
        doctorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}