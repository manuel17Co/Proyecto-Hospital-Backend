package com.meditech.hospital.appointment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.meditech.hospital.appointment.dto.AppointmentCreateRequest;
import com.meditech.hospital.appointment.dto.AppointmentResponse;
import com.meditech.hospital.appointment.entity.Appointment;
import com.meditech.hospital.appointment.entity.AppointmentStatus;
import com.meditech.hospital.appointment.repository.AppointmentRepository;
import com.meditech.hospital.doctors.entity.Doctor;
import com.meditech.hospital.doctors.repository.DoctorRepository;
import com.meditech.hospital.facility.entity.Facility;
import com.meditech.hospital.facility.repository.FacilityRepository;
import com.meditech.hospital.patients.entity.Patient;
import com.meditech.hospital.patients.repository.PatientRepository;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceCreateTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private FacilityRepository facilityRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    void create_linksPatientDoctorFacilityAndPersists() {
        Patient p = new Patient();
        p.setId(1L);
        p.setNombre("Ana");
        p.setApellido("Lopez");
        p.setDocumento("DOC1");
        p.setTelefono("555");
        p.setEstado(Patient.Estado.ACTIVO);

        Doctor d = new Doctor();
        d.setId(10L);
        d.setNombre("Dr");
        d.setApellido("House");
        d.setEspecialidad("Medicina");

        Facility f = new Facility();
        f.setId(5L);
        f.setNombre("Consultorio");
        f.setTipo("consulta");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(p));
        when(doctorRepository.findById(10L)).thenReturn(Optional.of(d));
        when(facilityRepository.findById(5L)).thenReturn(Optional.of(f));
        when(appointmentRepository.existsScheduleConflict(any(), eq(10L), eq(5L), isNull())).thenReturn(false);

        Instant ts = Instant.parse("2026-04-15T14:30:00Z");
        Appointment saved = new Appointment();
        saved.setId(99L);
        saved.setFechaHora(ts);
        saved.setEstado(AppointmentStatus.PENDIENTE);
        saved.setPaciente(p);
        saved.setMedico(d);
        saved.setInstalacion(f);
        saved.setNotas("nota");

        when(appointmentRepository.save(any(Appointment.class))).thenReturn(saved);

        AppointmentCreateRequest req = new AppointmentCreateRequest();
        req.setPacienteId(1L);
        req.setMedicoId(10L);
        req.setInstalacionId(5L);
        req.setFechaHora(ts.toString());
        req.setNotas("nota");

        AppointmentResponse response = appointmentService.create(req);

        assertThat(response.getId()).isEqualTo(99L);
        assertThat(response.getMedicoId()).isEqualTo(10L);
        assertThat(response.getInstalacionId()).isEqualTo(5L);
        assertThat(response.getEstado()).isEqualTo(AppointmentStatus.PENDIENTE);

        ArgumentCaptor<Appointment> captor = ArgumentCaptor.forClass(Appointment.class);
        verify(appointmentRepository).save(captor.capture());
        Appointment persisted = captor.getValue();
        assertThat(persisted.getPaciente().getId()).isEqualTo(1L);
        assertThat(persisted.getMedico().getId()).isEqualTo(10L);
        assertThat(persisted.getInstalacion().getId()).isEqualTo(5L);
    }
}
