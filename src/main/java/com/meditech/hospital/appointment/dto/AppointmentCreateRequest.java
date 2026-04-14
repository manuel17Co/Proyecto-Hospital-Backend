package com.meditech.hospital.appointment.dto;

import jakarta.validation.constraints.NotNull;

public class AppointmentCreateRequest {
    @NotNull
    private Long pacienteId;

    @NotNull
    private Long medicoId;

    @NotNull
    private Long instalacionId;

    @NotNull
    private String fechaHora;

    private String notas;

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
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

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
}
