package com.meditech.hospital.patients.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "patients")

public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    private String documento;
    private String telefono;
    
    @Enumerated (EnumType.STRING)
    private Estado estado;

    public enum Estado {
        ACTIVO, INACTIVO
    }
    public void setEstado(Estado estado) {
        this.estado = estado;
    }
    public long getId() {
        return id;
    }
    public String getNombre() {
        return nombre;

    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public String getDocumento() {
        return documento;
    }
    public void setDocumento(String documento) {
        this.documento = documento;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }


}