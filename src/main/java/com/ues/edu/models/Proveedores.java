/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.models;

/**
 *
 * @author mayel
 */
public class Proveedores {
   private int codigoProveedor;
   private String nombreProveedor;
   private String correo;
    private String dirrecion;
   private String numeroTelefono;
    private String numeroTelefono1;
private boolean estado;
    public Proveedores() {
    }

    public Proveedores(int codigoProveedor, String nombreProveedor, String correo, String dirrecion, String numeroTelefono, String numeroTelefono1, boolean estado) {
        this.codigoProveedor = codigoProveedor;
        this.nombreProveedor = nombreProveedor;
        this.correo = correo;
        this.dirrecion = dirrecion;
        this.numeroTelefono = numeroTelefono;
        this.numeroTelefono1 = numeroTelefono1;
        this.estado = estado;
    }

    public Proveedores(String nombreProveedor, String correo, String dirrecion, String numeroTelefono, String numeroTelefono1, boolean estado) {
        this.nombreProveedor = nombreProveedor;
        this.correo = correo;
        this.dirrecion = dirrecion;
        this.numeroTelefono = numeroTelefono;
        this.numeroTelefono1 = numeroTelefono1;
        this.estado = estado;
    }

   
    

    

    public int getCodigoProveedor() {
        return codigoProveedor;
    }

    public void setCodigoProveedor(int codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public String getDirrecion() {
        return dirrecion;
    }

    public void setDirrecion(String dirrecion) {
        this.dirrecion = dirrecion;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNumeroTelefono1() {
        return numeroTelefono1;
    }

    public void setNumeroTelefono1(String numeroTelefono1) {
        this.numeroTelefono1 = numeroTelefono1;
    }
    

    @Override
    public String toString() {
        return "Proveedores{" + "codigoProveedor=" + codigoProveedor + ", nombreProveedor=" + nombreProveedor + ", dirrecion=" + dirrecion + ", numeroTelefono=" + numeroTelefono + ", estado=" + estado + '}';
    }
    
    
}
