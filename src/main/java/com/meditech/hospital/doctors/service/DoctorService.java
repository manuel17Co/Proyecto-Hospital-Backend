package com.meditech.hospital.doctors.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.meditech.hospital.doctors.dto.CreateDoctorDto;
import com.meditech.hospital.doctors.entity.Doctor;
import com.meditech.hospital.doctors.repository.DoctorRepository;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> obtenerTodos() {
        return doctorRepository.findAll();
    }

    public Doctor obtenerPorId(Long id) {
        return doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("Médico no encontrado"));
    }

    public Doctor guardar(CreateDoctorDto dto) {
        Doctor doctor = new Doctor();
        doctor.setNombre(dto.getNombre());
        doctor.setApellido(dto.getApellido());
        doctor.setEspecialidad(dto.getEspecialidad());
        doctor.setTelefono(dto.getTelefono());
        return doctorRepository.save(doctor);
    }

    public Doctor actualizar(Long id, CreateDoctorDto dto) {
        Doctor doctor = obtenerPorId(id);
        doctor.setNombre(dto.getNombre());
        doctor.setApellido(dto.getApellido());
        doctor.setEspecialidad(dto.getEspecialidad());
        doctor.setTelefono(dto.getTelefono());
        return doctorRepository.save(doctor);
    }

    public void eliminar(Long id) {
        doctorRepository.deleteById(id);
    }
}