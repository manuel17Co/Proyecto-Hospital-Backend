package com.meditech.hospital.appointment.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.meditech.hospital.appointment.entity.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("""
        select a from Appointment a
        where (:estado is null or a.estado = :estado)
          and (
            :fechaInicio is null
            or (a.fechaHora >= :fechaInicio and a.fechaHora < :fechaFin)
          )
        order by a.fechaHora desc
        """)
    List<Appointment> findAllFiltered(@Param("estado") com.meditech.hospital.appointment.entity.AppointmentStatus estado,
                                      @Param("fechaInicio") Instant fechaInicio,
                                      @Param("fechaFin") Instant fechaFin);

    List<Appointment> findByPacienteIdOrderByFechaHoraDesc(Long pacienteId);

    List<Appointment> findByMedicoIdOrderByFechaHoraDesc(Long medicoId);

    List<Appointment> findByInstalacionIdOrderByFechaHoraDesc(Long instalacionId);

    @Query("""
        select count(a) > 0 from Appointment a
        where a.fechaHora = :fechaHora
          and a.estado <> com.meditech.hospital.appointment.entity.AppointmentStatus.CANCELADA
          and (
            a.medico.id = :medicoId
            or a.instalacion.id = :instalacionId
          )
          and (:excludeId is null or a.id <> :excludeId)
    """)
    boolean existsScheduleConflict(@Param("fechaHora") Instant fechaHora,
                                   @Param("medicoId") Long medicoId,
                                   @Param("instalacionId") Long instalacionId,
                                   @Param("excludeId") Long excludeId);
}
