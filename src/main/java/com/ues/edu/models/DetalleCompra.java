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
public class DetalleCompra {
    
    private int codigoDetalleCompra;
     private String codigo_producto;
     private String medida_producto;
     private Date fecha_vencimiento;
    private float precioCompra;
    private int porcentaje;
    private Compras compra;
    private int cantidad;
    private Productos productos;
    private Marca marca;
    private Medida medida;
    private float precioVentaUnitario;
    private float totalVenta;


    public DetalleCompra() {
    }

    public DetalleCompra(int codigoDetalleCompra, String codigo_producto, String medida_producto, Date fecha_vencimiento, float precioCompra, int porcentaje, Compras compra, int cantidad, Productos productos, Marca marca, Medida medida, float precioVentaUnitario, float totalVenta) {
        this.codigoDetalleCompra = codigoDetalleCompra;
        this.codigo_producto = codigo_producto;
        this.medida_producto = medida_producto;
        this.fecha_vencimiento = fecha_vencimiento;
        this.precioCompra = precioCompra;
        this.porcentaje = porcentaje;
        this.compra = compra;
        this.cantidad = cantidad;
        this.productos = productos;
        this.marca = marca;
        this.medida = medida;
        this.precioVentaUnitario = precioVentaUnitario;
        this.totalVenta = totalVenta;
    }

  
    public DetalleCompra(String codigo_producto, String medida_producto, Date fecha_vencimiento, float precioCompra, int porcentaje, Compras compra, int cantidad, Productos productos, Marca marca, Medida medida, float precioVentaUnitario, float totalVenta) {
        this.codigo_producto = codigo_producto;
        this.medida_producto = medida_producto;
        this.fecha_vencimiento = fecha_vencimiento;
        this.precioCompra = precioCompra;
        this.porcentaje = porcentaje;
        this.compra = compra;
        this.cantidad = cantidad;
        this.productos = productos;
        this.marca = marca;
        this.medida = medida;
        this.precioVentaUnitario = precioVentaUnitario;
        this.totalVenta = totalVenta;
    }

  

 
    public int getCodigoDetalleCompra() {
        return codigoDetalleCompra;
    }

    public void setCodigoDetalleCompra(int codigoDetalleCompra) {
        this.codigoDetalleCompra = codigoDetalleCompra;
    }

    public float getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(float precioCompra) {
        this.precioCompra = precioCompra;
    }

    public Compras getCompra() {
        return compra;
    }

    public void setCompra(Compras compra) {
        this.compra = compra;
    }

    public int getCantidad() {
        return cantidad;
    }

    public float getPrecioVentaUnitario() {
        return precioVentaUnitario;
    }

    public void setPrecioVentaUnitario(float precioVentaUnitario) {
        this.precioVentaUnitario = precioVentaUnitario;
    }

    public float getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(float totalVenta) {
        this.totalVenta = totalVenta;
    }
    

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Productos getProductos() {
        return productos;
    }

    public void setProductos(Productos productos) {
        this.productos = productos;
    }

    public String getCodigo_producto() {
        return codigo_producto;
    }

    public void setCodigo_producto(String codigo_producto) {
        this.codigo_producto = codigo_producto;
    }

    public String getMedida_producto() {
        return medida_producto;
    }

    public void setMedida_producto(String medida_producto) {
        this.medida_producto = medida_producto;
    }

    public Date getFecha_vencimiento() {
        return fecha_vencimiento;
    }

    public void setFecha_vencimiento(Date fecha_vencimiento) {
        this.fecha_vencimiento = fecha_vencimiento;
    }

    public int getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(int porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public Medida getMedida() {
        return medida;
    }

    public void setMedida(Medida medida) {
        this.medida = medida;
    }
    
    
    
    
}
