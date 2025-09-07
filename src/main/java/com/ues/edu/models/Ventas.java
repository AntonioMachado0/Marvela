/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.models;

import java.sql.Date;
import java.sql.Time;

/**
 *
 * @author mayel
 */
public class Ventas {
    private int codigoVenta;
    private int numeroVenta;
    private Time hora;
    private Date fecha;
    private Empleado empleado;

    public Ventas() {
    }

    public Ventas(int codigoVenta, int numeroVenta, Time hora, Date fecha, Empleado empleado) {
        this.codigoVenta = codigoVenta;
        this.numeroVenta = numeroVenta;
        this.hora = hora;
        this.fecha = fecha;
        this.empleado = empleado;
    }

    public Ventas(int numeroVenta, Time hora, Date fecha, Empleado empleado) {
        this.numeroVenta = numeroVenta;
        this.hora = hora;
        this.fecha = fecha;
        this.empleado = empleado;
    }

    public int getCodigoVenta() {
        return codigoVenta;
    }

    public void setCodigoVenta(int codigoVenta) {
        this.codigoVenta = codigoVenta;
    }

    public int getNumeroVenta() {
        return numeroVenta;
    }

    public void setNumeroVenta(int numeroVenta) {
        this.numeroVenta = numeroVenta;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    
    
    
}
