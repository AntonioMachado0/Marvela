/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.models;

/**
 *
 * @author mayel
 */
public class Usuario {
      
 private   int codigoUsuario;
 private   String correo;
 private   String contraseña;
 private   boolean estado;
 private   Empleado empleado;

    public Usuario() {
    }

    public Usuario(int codigoUsuario, String correo, String contraseña, boolean estado, Empleado empleado) {
        this.codigoUsuario = codigoUsuario;
        this.correo = correo;
        this.contraseña = contraseña;
        this.estado = estado;
        this.empleado = empleado;
    }

    public Usuario(String correo, String contraseña, boolean estado, Empleado empleado) {
        this.correo = correo;
        this.contraseña = contraseña;
        this.estado = estado;
        this.empleado = empleado;
    }

    public int getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(int codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }
    
    
    
}
