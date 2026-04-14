package com.meditech.hospital.appointment.dto;

import com.meditech.hospital.appointment.entity.AppointmentStatus;
import com.meditech.hospital.patients.dto.PacienteResponse;

public class AppointmentResponse {
    private Long id;
    private String fechaHora;
    private AppointmentStatus estado;
    private PacienteResponse paciente;
    private Long medicoId;
    private Long instalacionId;
    private String notas;

    public AppointmentResponse() {}

    public AppointmentResponse(Long id, String fechaHora, AppointmentStatus estado, PacienteResponse paciente, Long medicoId, Long instalacionId, String notas) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.estado = estado;
        this.paciente = paciente;
        this.medicoId = medicoId;
        this.instalacionId = instalacionId;
        this.notas = notas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public AppointmentStatus getEstado() {
        return estado;
    }

    public void setEstado(AppointmentStatus estado) {
        this.estado = estado;
    }

    public PacienteResponse getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteResponse paciente) {
        this.paciente = paciente;
    }

    public Long getMedicoId() {
        return medicoId;
    }

    public void setMedicoId(Long medicoId) {
        this.medicoId = medicoId;
    }

    public Long getInstalacionId() {
        return instalacionId;
    }

    public void setInstalacionId(Long instalacionId) {
        this.instalacionId = instalacionId;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
}
