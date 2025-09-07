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
public class Productos {
    private int idProducto;
    private String codigoProducto;
    private String descripcion;
    private String unidadMedida;
    private String marca;
    private Date fechaVencimiento;
    private Categoria categoria;
    private float porcentaje;

    public Productos() {
    }

    public Productos(int idProducto, String codigoProducto, String descripcion, String unidadMedida, String marca, Date fechaVencimiento, Categoria categoria, float porcentaje) {
        this.idProducto = idProducto;
        this.codigoProducto = codigoProducto;
        this.descripcion = descripcion;
        this.unidadMedida = unidadMedida;
        this.marca = marca;
        this.fechaVencimiento = fechaVencimiento;
        this.categoria = categoria;
        this.porcentaje = porcentaje;
    }

    public Productos(String codigoProducto, String descripcion, String unidadMedida, String marca, Date fechaVencimiento, Categoria categoria, float porcentaje) {
        this.codigoProducto = codigoProducto;
        this.descripcion = descripcion;
        this.unidadMedida = unidadMedida;
        this.marca = marca;
        this.fechaVencimiento = fechaVencimiento;
        this.categoria = categoria;
        this.porcentaje = porcentaje;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public float getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(float porcentaje) {
        this.porcentaje = porcentaje;
    }

    
}