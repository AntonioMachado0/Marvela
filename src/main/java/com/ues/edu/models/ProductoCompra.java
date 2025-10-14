/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.models;

import java.util.Date;

/**
 *
 * @author mayel
 */

//ESTA CLASE ES  UN DTO
public class ProductoCompra {
    private String nombreProducto;
    private int cantidadProducto;
    private String nombreMarca;
    private String codigoProducto;
    //private String imagen;
    private double precioVenta;
    private String cantidadMedida;
    private String nombreMedida;
    private String nombreCategoria;
    private Date fechaCompra;
    private Date fechaVencimiento;
    private String imagen;
     private String tipoImagen;
    
    // Constructor vacío
    public ProductoCompra() {}

    public ProductoCompra(String nombreProducto, int cantidadProducto, String nombreMarca, String codigoProducto, double precioVenta, String cantidadMedida, String nombreMedida, String nombreCategoria, Date fechaCompra, Date fechaVencimiento,String imagen) {
        this.nombreProducto = nombreProducto;
        this.cantidadProducto = cantidadProducto;
        this.nombreMarca = nombreMarca;
        this.codigoProducto = codigoProducto;
        this.precioVenta = precioVenta;
        this.cantidadMedida = cantidadMedida;
        this.nombreMedida = nombreMedida;
        this.nombreCategoria = nombreCategoria;
        this.fechaCompra = fechaCompra;
        this.fechaVencimiento = fechaVencimiento;
        this.imagen = imagen;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public int getCantidadProducto() {
        return cantidadProducto;
    }

    public void setCantidadProducto(int cantidadProducto) {
        this.cantidadProducto = cantidadProducto;
    }

    public String getNombreMarca() {
        return nombreMarca;
    }

    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public String getCantidadMedida() {
        return cantidadMedida;
    }

    public void setCantidadMedida(String cantidadMedida) {
        this.cantidadMedida = cantidadMedida;
    }

    public String getNombreMedida() {
        return nombreMedida;
    }

    public void setNombreMedida(String nombreMedida) {
        this.nombreMedida = nombreMedida;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    
     public String getTipoImagen() { 
         return tipoImagen;
     }
    public void setTipoImagen(String tipoImagen) { 
        this.tipoImagen = tipoImagen;
    }
}