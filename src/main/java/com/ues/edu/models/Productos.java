/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.models;

import java.io.InputStream;
import java.sql.Date;

/**
 *
 * @author mayel
 */
public class Productos {
    private int idProducto;
    private String codigoProducto;
     private String nombre;
    private String descripcion;
 private byte[]  imagen;
    
    private Medida medida;
    private Marca marca;
    private Date fechaVencimiento;
    private Categoria categoria;
    
    private float porcentaje;

    public Productos() {
    }

    public Productos(int idProducto, String codigoProducto, String nombre, String descripcion, byte[] imagen, Medida medida, Marca marca, Date fechaVencimiento, Categoria categoria, float porcentaje) {
        this.idProducto = idProducto;
        this.codigoProducto = codigoProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.medida = medida;
        this.marca = marca;
        this.fechaVencimiento = fechaVencimiento;
        this.categoria = categoria;
        this.porcentaje = porcentaje;
    }

    public Productos(String codigoProducto, String nombre, String descripcion, byte[] imagen, Medida medida, Marca marca, Date fechaVencimiento, Categoria categoria, float porcentaje) {
        this.codigoProducto = codigoProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.medida = medida;
        this.marca = marca;
        this.fechaVencimiento = fechaVencimiento;
        this.categoria = categoria;
        this.porcentaje = porcentaje;
    }

    public byte[] getImagen() { return imagen; }
public void setImagen(byte[] imagen) { this.imagen = imagen; }

    
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    

    
    
    public Medida getMedida() {
        return medida;
    }

    public void setMedida(Medida medida) {
        this.medida = medida;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    
}