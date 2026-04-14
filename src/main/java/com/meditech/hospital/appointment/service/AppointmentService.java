package com.meditech.hospital.appointment.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.stereotype.Service;

import com.meditech.hospital.appointment.dto.AppointmentCreateRequest;
import com.meditech.hospital.appointment.dto.AppointmentResponse;
import com.meditech.hospital.appointment.dto.AppointmentStatusRequest;
import com.meditech.hospital.appointment.entity.Appointment;
import com.meditech.hospital.appointment.entity.AppointmentStatus;
import com.meditech.hospital.appointment.repository.AppointmentRepository;
import com.meditech.hospital.common.exception.BadRequestException;
import com.meditech.hospital.common.exception.NotFoundException;
import com.meditech.hospital.doctors.entity.Doctor;
import com.meditech.hospital.doctors.repository.DoctorRepository;
import com.meditech.hospital.facility.entity.Facility;
import com.meditech.hospital.facility.repository.FacilityRepository;
import com.meditech.hospital.patients.dto.PacienteResponse;
import com.meditech.hospital.patients.entity.Patient;
import com.meditech.hospital.patients.repository.PatientRepository;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final FacilityRepository facilityRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository,
                              FacilityRepository facilityRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.facilityRepository = facilityRepository;
    }

    public List<AppointmentResponse> list(AppointmentStatus estado, LocalDate fecha) {
        return appointmentRepository.findAllFiltered(estado, fecha)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public AppointmentResponse getById(Long id) {
        Appointment a = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada"));
        return toResponse(a);
    }

    public AppointmentResponse create(AppointmentCreateRequest request) {
        Instant fechaHora = parseFechaHora(request.getFechaHora());

        Patient paciente = patientRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new NotFoundException("Paciente no encontrado"));

        Doctor medico = doctorRepository.findById(request.getMedicoId())
                .orElseThrow(() -> new NotFoundException("Médico no encontrado"));

        Facility instalacion = facilityRepository.findById(request.getInstalacionId())
                .orElseThrow(() -> new NotFoundException("Instalación no encontrada"));

        if (appointmentRepository.existsScheduleConflict(fechaHora, medico.getId(), instalacion.getId(), null)) {
            throw new BadRequestException("Conflicto de horario para médico o instalación");
        }

        Appointment a = new Appointment();
        a.setFechaHora(fechaHora);
        a.setEstado(AppointmentStatus.PENDIENTE);
        a.setPaciente(paciente);
        a.setMedico(medico);
        a.setInstalacion(instalacion);
        a.setNotas(request.getNotas());

        Appointment saved = appointmentRepository.save(a);
        return toResponse(saved);
    }

    public AppointmentResponse update(Long id, AppointmentCreateRequest request) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada"));

        Instant fechaHora = parseFechaHora(request.getFechaHora());

        Patient paciente = patientRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new NotFoundException("Paciente no encontrado"));

        Doctor medico = doctorRepository.findById(request.getMedicoId())
                .orElseThrow(() -> new NotFoundException("Médico no encontrado"));

        Facility instalacion = facilityRepository.findById(request.getInstalacionId())
                .orElseThrow(() -> new NotFoundException("Instalación no encontrada"));

        if (appointmentRepository.existsScheduleConflict(fechaHora, medico.getId(), instalacion.getId(), existing.getId())) {
            throw new BadRequestException("Conflicto de horario para médico o instalación");
        }

        existing.setFechaHora(fechaHora);
        existing.setPaciente(paciente);
        existing.setMedico(medico);
        existing.setInstalacion(instalacion);
        existing.setNotas(request.getNotas());

        Appointment saved = appointmentRepository.save(existing);
        return toResponse(saved);
    }

    public AppointmentResponse updateStatus(Long id, AppointmentStatusRequest request) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada"));

        AppointmentStatus current = existing.getEstado();
        AppointmentStatus target = request.getEstado();

        if (!isTransitionAllowed(current, target)) {
            throw new BadRequestException("Transición de estado no permitida");
        }

        existing.setEstado(target);
        Appointment saved = appointmentRepository.save(existing);
        return toResponse(saved);
    }

    public void delete(Long id) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada"));
        appointmentRepository.delete(existing);
    }

    private boolean isTransitionAllowed(AppointmentStatus from, AppointmentStatus to) {
        if (from == null || to == null) {
            return false;
        }
        if (from == to) {
            return true;
        }
        return switch (from) {
            case PENDIENTE -> to == AppointmentStatus.CONFIRMADA || to == AppointmentStatus.CANCELADA;
            case CONFIRMADA -> to == AppointmentStatus.COMPLETADA || to == AppointmentStatus.CANCELADA;
            case CANCELADA, COMPLETADA -> false;
        };
    }

    private Instant parseFechaHora(String fechaHora) {
        try {
            return Instant.parse(fechaHora);
        } catch (Exception e) {
            throw new BadRequestException("fechaHora inválida, se espera ISO-8601 (ej: 2026-04-15T10:00:00Z)");
        }
    }

    private AppointmentResponse toResponse(Appointment a) {
        Patient p = a.getPaciente();
        PacienteResponse paciente = new PacienteResponse(
                p.getId(),
                p.getNombre(),
                p.getApellido(),
                p.getDocumento(),
                p.getTelefono(),
                p.getEstado()
        );

        return new AppointmentResponse(
                a.getId(),
                a.getFechaHora() == null ? null : a.getFechaHora().toString(),
                a.getEstado(),
                paciente,
                a.getMedico() == null ? null : a.getMedico().getId(),
                a.getInstalacion() == null ? null : a.getInstalacion().getId(),
                a.getNotas()
        );
    }
}
