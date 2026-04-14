package com.meditech.hospital.appointment.dto;

import com.meditech.hospital.appointment.entity.AppointmentStatus;

import jakarta.validation.constraints.NotNull;

public class AppointmentStatusRequest {
    @NotNull
    private AppointmentStatus estado;

    public AppointmentStatus getEstado() {
        return estado;
    }

    public void setEstado(AppointmentStatus estado) {
        this.estado = estado;
    }
}
