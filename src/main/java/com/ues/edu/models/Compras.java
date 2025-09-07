/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.models;

import java.sql.Date;

/**
 *
 * @author mayel
 */
public class Compras {

    private int idCompra;
    private Date fechaCompra;
    private Proveedores proveedores;
    private String numeroOrden;
    private Empleado empleado;

    public Compras() {
    }

    public Compras(int idCompra, Date fechaCompra, Proveedores proveedores, String numeroOrden, Empleado empleado) {
        this.idCompra = idCompra;
        this.fechaCompra = fechaCompra;
        this.proveedores = proveedores;
        this.numeroOrden = numeroOrden;
        this.empleado = empleado;
    }

    public Compras(Date fechaCompra, Proveedores proveedores, String numeroOrden, Empleado empleado) {
        this.fechaCompra = fechaCompra;
        this.proveedores = proveedores;
        this.numeroOrden = numeroOrden;
        this.empleado = empleado;
    }

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public Proveedores getProveedores() {
        return proveedores;
    }

    public void setProveedores(Proveedores proveedores) {
        this.proveedores = proveedores;
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(String numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }
    
    
}
