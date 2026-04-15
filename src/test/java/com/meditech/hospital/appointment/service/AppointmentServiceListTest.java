package com.meditech.hospital.appointment.service;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
class AppointmentServiceListTest {

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
    void list_passesUtcDayRangeWhenFechaProvided() {
        LocalDate fecha = LocalDate.of(2026, 4, 15);
        Instant expectedStart = fecha.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant expectedEnd = fecha.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        Patient p = new Patient();
        p.setId(1L);
        p.setNombre("n");
        p.setApellido("a");
        p.setDocumento("d");
        p.setTelefono("t");
        p.setEstado(Patient.Estado.ACTIVO);

        Doctor d = new Doctor();
        d.setId(2L);
        Facility f = new Facility();
        f.setId(3L);

        Appointment a = new Appointment();
        a.setId(10L);
        a.setFechaHora(expectedStart);
        a.setEstado(AppointmentStatus.PENDIENTE);
        a.setPaciente(p);
        a.setMedico(d);
        a.setInstalacion(f);

        when(appointmentRepository.findAllFiltered(isNull(), eq(expectedStart), eq(expectedEnd)))
                .thenReturn(List.of(a));

        appointmentService.list(null, fecha);

        verify(appointmentRepository).findAllFiltered(isNull(), eq(expectedStart), eq(expectedEnd));
    }

    @Test
    void list_passesNullRangeWhenFechaNotProvided() {
        when(appointmentRepository.findAllFiltered(isNull(), isNull(), isNull())).thenReturn(List.of());

        appointmentService.list(null, null);

        verify(appointmentRepository).findAllFiltered(isNull(), isNull(), isNull());
    }

    @Test
    void listByPatientId_returnsAppointmentsForPatient() {
        Patient p = new Patient();
        p.setId(1L);
        p.setNombre("n");
        p.setApellido("a");
        p.setDocumento("d");
        p.setTelefono("t");
        p.setEstado(Patient.Estado.ACTIVO);

        Doctor d = new Doctor();
        d.setId(2L);
        Facility f = new Facility();
        f.setId(3L);

        Appointment a = new Appointment();
        a.setId(10L);
        a.setFechaHora(Instant.parse("2026-04-15T10:00:00Z"));
        a.setEstado(AppointmentStatus.PENDIENTE);
        a.setPaciente(p);
        a.setMedico(d);
        a.setInstalacion(f);

        when(patientRepository.findById(1L)).thenReturn(java.util.Optional.of(p));
        when(appointmentRepository.findByPacienteIdOrderByFechaHoraDesc(1L)).thenReturn(List.of(a));

        var result = appointmentService.listByPatientId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPaciente().getId()).isEqualTo(1L);
        verify(appointmentRepository).findByPacienteIdOrderByFechaHoraDesc(1L);
    }

    @Test
    void listByDoctorId_returnsAppointmentsForDoctor() {
        Doctor d = new Doctor();
        d.setId(2L);

        when(doctorRepository.findById(2L)).thenReturn(java.util.Optional.of(d));
        when(appointmentRepository.findByMedicoIdOrderByFechaHoraDesc(2L)).thenReturn(List.of());

        var result = appointmentService.listByDoctorId(2L);

        assertThat(result).isEmpty();
        verify(appointmentRepository).findByMedicoIdOrderByFechaHoraDesc(2L);
    }

    @Test
    void listByFacilityId_returnsAppointmentsForFacility() {
        Facility f = new Facility();
        f.setId(3L);

        when(facilityRepository.findById(3L)).thenReturn(java.util.Optional.of(f));
        when(appointmentRepository.findByInstalacionIdOrderByFechaHoraDesc(3L)).thenReturn(List.of());

        var result = appointmentService.listByFacilityId(3L);

        assertThat(result).isEmpty();
        verify(appointmentRepository).findByInstalacionIdOrderByFechaHoraDesc(3L);
    }
}
